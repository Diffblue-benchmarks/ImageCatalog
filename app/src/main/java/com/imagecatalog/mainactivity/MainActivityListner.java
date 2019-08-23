package com.imagecatalog.mainactivity;

import org.json.JSONException;

public interface MainActivityListner {

    void onCatalogDetailFailed(String customModalDataHolder);

    void onCatalogDetailSuccess(String customModalDataHolder) throws JSONException;

    void onCatalogSuccess(String customModalDataHolder) throws JSONException;

}
