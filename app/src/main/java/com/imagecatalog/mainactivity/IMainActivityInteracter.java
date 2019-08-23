package com.imagecatalog.mainactivity;

import org.json.JSONException;

import java.io.IOException;

public interface IMainActivityInteracter {

    void fetchOlderUser(MainActivityPresenter listener);

    void fetchFirstSetUser(MainActivityPresenter listener);

    void fetchNewSetUser(MainActivityPresenter listener) throws IOException, JSONException;
}
