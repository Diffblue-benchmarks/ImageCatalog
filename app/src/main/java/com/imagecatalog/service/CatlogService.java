package com.imagecatalog.service;

public class CatlogService extends CatalogServiceAbstract {
    private static final String URL = CatalogServiceUtils.getBaseUrl();
    private static final String TAG = "CatlogService";
    public static String keyId = "";
    public static String imagePost = "";
    public static String textPost = "";
    public static String confidencePost;


    @Override
    public String getUrl() {
        String requestUrl = URL;
        return requestUrl;
    }

}
