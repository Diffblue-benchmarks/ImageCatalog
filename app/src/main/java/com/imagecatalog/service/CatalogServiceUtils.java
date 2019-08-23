package com.imagecatalog.service;

import com.imagecatalog.utils.NativeConstants;

public class CatalogServiceUtils {
    private CatalogServiceUtils() {
    }

    public static String getBaseUrl() {
        return NativeConstants.BASE_URL;
    }

    public static String[] getSHA256() {
        return NativeConstants.SHA256;
    }
}
