package com.imagecatalog.mainactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.imagecatalog.CustomModal;
import com.imagecatalog.service.CatlogService;
import com.imagecatalog.utils.MGLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivityPresenter implements IMainActivityPresenter, MainActivityListner {

    private IMainActivityInteracter mInteractor;
    private List<CustomModal> imageObject;
    private MainActivityView mView;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 1;

    public MainActivityPresenter(MainActivityView view) {
        mView = view;
        mInteractor = new MainActivityInteracter();
    }

    @Override
    public void fetchOlderUser() {
        mInteractor.fetchOlderUser(this);
    }

    @Override
    public void fetchFirstSetUser() {
        mInteractor.fetchFirstSetUser(this);
    }

    @Override
    public void fetchNewSetUser() {
        try {
            mInteractor.fetchNewSetUser(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCatalogDetailFailed(String customModalDataHolder) {
        MGLog.d(TAG, "onCatalogDetailFailed !!!= " + customModalDataHolder);
    }

    public List<CustomModal> formCatalog(String responseData) throws JSONException {
        boolean firstCatalogBool = false;
        imageObject = new ArrayList<CustomModal>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(responseData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject customModalObj = null;
            try {
                customModalObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String largeImageURL = customModalObj.getString("img");
            byte[] decodedString = Base64.decode(largeImageURL, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            String completeDesc = customModalObj.getString("text");
            double confidence = customModalObj.getDouble("confidence");
            String confidenceValue = String.valueOf(confidence);
            String keyId = customModalObj.getString("_id");

            CatlogService.keyId = keyId;

            int firstCatalogInt = 0;
            firstCatalogInt++;
            if (firstCatalogInt == 1) {
                CatlogService.confidencePost = confidenceValue;
                CatlogService.imagePost = largeImageURL;
                CatlogService.textPost = completeDesc;
            }

            CustomModal customModal = new CustomModal(decodedByte, completeDesc, confidenceValue, keyId);

            imageObject.add(customModal);
        }
        return imageObject;
    }

    @Override
    public void onCatalogDetailSuccess(String responseData) throws JSONException {
        formCatalog(responseData);
        mView.onResponseForAdapter(imageObject);
    }

    @Override
    public void onCatalogSuccess(String responseData) throws JSONException {
        formCatalog(responseData);
        mView.onResponseForAdapter(imageObject);
    }
}

