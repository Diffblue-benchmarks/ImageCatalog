package com.imagecatalog.service;

import androidx.annotation.NonNull;

public interface ICatalogServiceListener<T> {

    void onFailure(@NonNull Exception e);
}
