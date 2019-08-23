package com.imagecatalog;

import android.graphics.Bitmap;

import java.io.Serializable;


public class CustomModal implements Serializable {
    private String description;
    private String confidenceValue;
    private String picId;
    private Bitmap decodedByte;

    public CustomModal(Bitmap decodedByte, String description, String confidenceValue, String picId) {
        this.decodedByte = decodedByte;
        this.description = description;
        this.confidenceValue = confidenceValue;
        this.picId = picId;
    }

    public Bitmap getDecodedByte() {
        return decodedByte;
    }

    public void setDecodedByte(Bitmap decodedByte) {
        this.decodedByte = decodedByte;
    }


    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(String confidenceValue) {
        this.confidenceValue = confidenceValue;
    }
}
