package com.imagecatalog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public abstract class PaginationListner extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public PaginationListner(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                try {
                    loadMoreItems();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        // For loading newest items added in server
        if (firstVisibleItemPosition == 0 && dy > 0) {
//                loadMostRecentItems();

        }


    }


    protected abstract void loadMoreItems() throws JSONException;

    public abstract boolean isLoading();

    public abstract void loadMostRecentItems() throws JSONException;
}
