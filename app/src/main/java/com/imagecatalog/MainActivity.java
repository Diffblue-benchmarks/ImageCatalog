package com.imagecatalog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.imagecatalog.mainactivity.MainActivityPresenter;
import com.imagecatalog.mainactivity.MainActivityView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    RecyclerView recyclerView;
    MainActivityView serviceRequest;
    Context context;
    private List<CustomModal> imageObject;
    private ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 1;
    private MainActivityPresenter mPresenter;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceRequest = MainActivity.this;
        imageObject = new ArrayList<CustomModal>();
        mPresenter = new MainActivityPresenter(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomAdapter(this, imageObject);
        // set the adapter object to the Recyclerview
        recyclerView.setAdapter(mAdapter);

        firstCallService();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

//        progressBar = findViewById(R.id.progress_bar);

        recyclerView.addOnScrollListener(new PaginationListner(linearLayoutManager) {
            @Override
            protected void loadMoreItems() throws JSONException {
                progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
                if (!isLastPage) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                loadData(page);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 200);
                }

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void loadMostRecentItems() throws JSONException {
                isLastPage = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadNewData(page);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 200);
            }
        });

    }

    private void loadNewData(int page) throws JSONException {
        mPresenter.fetchNewSetUser();
        isLastPage = false;
//        progressBar.setVisibility(View.GONE);
    }

    private void loadData(int page) throws JSONException {
        mPresenter.fetchOlderUser();
        isLoading = false;
//        progressBar.setVisibility(View.GONE);
    }

    public void firstCallService() {
        mPresenter.fetchFirstSetUser();
    }


    @Override
    public void onResponse(final List<CustomModal> response) {

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.addItems(response);
            }
        });
    }

    @Override
    public void onResponseForAdapter(List<CustomModal> response) {
        serviceRequest.onResponse(response);
    }
}
