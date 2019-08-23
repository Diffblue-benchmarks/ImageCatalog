package com.imagecatalog.mainactivity;

import com.imagecatalog.CustomModal;

import java.util.List;

public interface MainActivityView {
    void onResponseForAdapter(List<CustomModal> response);

    void onResponse(List<CustomModal> response);
}
