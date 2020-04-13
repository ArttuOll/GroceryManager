package com.bsuuv.grocerymanager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class SearchFoodItemActivity extends FragmentActivity implements DownloadCallback {

    private List<FoodItem> mFoodItems;
    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //executeSearch(query);
        }
    }

    @Override
    public void updateFromDownload(List<FoodItem> result) {
        if (!result.isEmpty()) {
            mFoodItems = result;
        } else {
            Log.d("DOWNLOAD", "connectivity error");
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fooditem_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.fooditem_search).getActionView();
        //TODO: searchView kuntoon sitten, kun searchable configuration ja activity valmiit.
        return super.onCreateOptionsMenu(menu);
    }
}
