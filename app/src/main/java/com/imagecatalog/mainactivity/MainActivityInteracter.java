package com.imagecatalog.mainactivity;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.imagecatalog.service.CatlogService;
import com.imagecatalog.service.ICatalogServiceListener;
import com.imagecatalog.utils.MGLog;

import org.json.JSONException;

import java.io.IOException;

public class MainActivityInteracter implements IMainActivityInteracter {
    private static final String TAG = "MainActivityInteracter";
    String responseData = "";

    @Override
    public void fetchOlderUser(final MainActivityPresenter listener) {

        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {
            private Exception mException;

            @Override
            protected String doInBackground(String... responses) {
                CatlogService service = new CatlogService();
                service.getApi(new ICatalogServiceListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MGLog.e(TAG, e.toString());
                        mException = e;
                    }
                });
                try {
                    responseData = service.response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return responseData;
            }

            @Override
            protected void onPostExecute(String responseData) {
                if (mException != null) {
                    listener.onCatalogDetailFailed(responseData);
                } else {
                    try {
                        listener.onCatalogDetailSuccess(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @Override
    public void fetchFirstSetUser(final MainActivityPresenter listener) {
        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {
            private Exception mException;

            @Override
            protected String doInBackground(String... responses) {
                CatlogService service = new CatlogService();
                service.firstUserBuilder(new ICatalogServiceListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MGLog.e(TAG, e.toString());
                        mException = e;
                    }
                });
                try {
                    responseData = service.response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return responseData;
            }

            @Override
            protected void onPostExecute(String responseData) {
                if (mException != null) {
                    listener.onCatalogDetailFailed(responseData);
                } else {
                    try {
                        listener.onCatalogDetailSuccess(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @Override
    public void fetchNewSetUser(final MainActivityPresenter listener) throws IOException, JSONException {

        CatlogService service = new CatlogService();
        try {
            service.newCatalogBuilder(new ICatalogServiceListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    MGLog.e(TAG, e.toString());

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        responseData = service.response.body().string();

        listener.onCatalogDetailSuccess(responseData);
    }
}
