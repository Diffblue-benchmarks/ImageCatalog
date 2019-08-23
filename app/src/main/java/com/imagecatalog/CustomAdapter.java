package com.imagecatalog;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ModalViewHolder> {
    Context context;
    private List<CustomModal> modalList;

    public CustomAdapter(Context mContext, List<CustomModal> customList) {
        this.context = mContext;
        this.modalList = customList;
    }


    @Override
    public ModalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.catalog_row, null);
        ModalViewHolder modalViewHolder = new ModalViewHolder(itemLayoutView);
        return modalViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(ModalViewHolder modalViewHolder, int position) {
        CustomModal singleCatalog = modalList.get(position);

        modalViewHolder.catalog_item = singleCatalog;

        modalViewHolder.txt_name.setText(singleCatalog.getDescription());
        modalViewHolder.txt_picID.setText(singleCatalog.getPicId());
        modalViewHolder.img_photo.setImageBitmap(singleCatalog.getDecodedByte());
        modalViewHolder.txt_confidence.setText(singleCatalog.getConfidenceValue());

    }

    // Returns the size
    @Override
    public int getItemCount() {
        return modalList.size();
    }

    public void addItems(List<CustomModal> items) {
        modalList.addAll(items);
        notifyDataSetChanged();
    }

    public static class ModalViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;
        public TextView txt_confidence;
        public TextView txt_picID;
        public ImageView img_photo;
        public CustomModal catalog_item;

        public ModalViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txt_name = itemLayoutView.findViewById(R.id.txt_name);
            txt_confidence = itemLayoutView.findViewById(R.id.txt_confidence);
            txt_picID = itemLayoutView.findViewById(R.id.txt_picId);
            img_photo = itemLayoutView.findViewById(R.id.img_photo);
        }

    }


}