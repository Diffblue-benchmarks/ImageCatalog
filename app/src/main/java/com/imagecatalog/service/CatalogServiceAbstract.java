package com.imagecatalog.service;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.imagecatalog.utils.MGLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.CertificatePinner;
import okhttp3.Headers;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public abstract class CatalogServiceAbstract<T> {
    public static final String SSL = "SSL";
    private static boolean isValidCertificate = true;
    public Response response;
    private OkHttpClient client;

    private static SSLSocketFactory getSSLSocketFactory(TrustManager[] trustAllCerts) {
        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {

        }
        return null;
    }

    @NonNull
    private static TrustManager[] getTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        try {
                            chain[0].checkValidity();
                        } catch (Exception e) {
                            throw new CertificateException("Certificate not valid or trusted.");
                        }
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        try {
                            chain[0].checkValidity();
                        } catch (Exception e) {
                            throw new CertificateException("Certificate not valid or trusted.");
                        }
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

    public abstract String getUrl();

    public void getApi(ICatalogServiceListener listener) {
        String mDetailUrl = getUrl() + "?max_id=" + CatlogService.keyId;
        MGLog.d(TAG, "getApi url=" + mDetailUrl);
        Request request = new Request.Builder()
                .url(mDetailUrl)
                .headers(getHeaders()
                        .build()).build();
        response = executeCall(request, listener);
    }

    public void newCatalogBuilder(ICatalogServiceListener listener) throws JSONException, IOException {
        String mDetailUrl = getUrl();

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("text", CatlogService.textPost);
        jsonParams.put("img", CatlogService.imagePost);
        jsonParams.put("confidence", CatlogService.confidencePost);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        Request request = new Request.Builder()
                .url(mDetailUrl)
                .post(requestBody)
                .headers(getHeaders()
                        .build())
                .build();

//        responseNew = (OkHttpClient) executeCallNew(request, listener);
    }


    public void firstUserBuilder(ICatalogServiceListener listener) {
        String mDetailUrl = getUrl();
        Request request = new Request.Builder()
                .url(mDetailUrl)
                .cacheControl(new CacheControl.Builder().build())
                .headers(getHeaders()
                        .build()).build();
        response = executeCall(request, listener);
    }

    public Headers.Builder getHeaders() {
        Headers.Builder builder = new Headers.Builder();
        builder.add("Authorization", "41033e41c69630bc8e21f3eaf26798ac");
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        String cookie = cookieManager.getCookie(getUrl());
        if (cookie != null) {
            MGLog.d(TAG, "Cookie = " + cookie);
            builder.add("Cookie", cookie);
        }
        return builder;
    }

    private Response executeCall(Request request, ICatalogServiceListener listener) {
        Response responseCall = null;
        try {
            responseCall = getOkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            // In case of error, logging exception to get to know which call was the one it failed
            MGLog.e(TAG, String.valueOf(e));
            listener.onFailure(e);
        }
        return responseCall;
    }

    public OkHttpClient getOkHttpClient() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient client = null;
        if (isValidCertificate) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .certificatePinner(getCertificatePinner())
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();
        } else {
            final TrustManager[] trustAllCerts = getTrustManagers();
            final SSLSocketFactory sslSocketFactory = getSSLSocketFactory(trustAllCerts);
            if (sslSocketFactory != null) {
                client = new OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS)
                        .cookieJar(new JavaNetCookieJar(cookieManager))
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                try {
                                    URL url = new URL(CatalogServiceUtils.getBaseUrl());
                                    if (url != null && url.getHost().equals(hostname))
                                        return true;
                                } catch (MalformedURLException e) {
                                    MGLog.e(TAG, e.getMessage());
                                }
                                return false;
                            }
                        }).build();
            }
        }

        return client;
    }

    private CertificatePinner getCertificatePinner() {
        MGLog.d(TAG, "Using certificate pinner..!");
        String[] sha256 = CatalogServiceUtils.getSHA256();
        CertificatePinner.Builder certificatePinner = new CertificatePinner.Builder();
        isValidCertificate = false;
        ICatalogServiceListener listener = null;
        firstUserBuilder(listener);
        return certificatePinner.build();
    }

}
