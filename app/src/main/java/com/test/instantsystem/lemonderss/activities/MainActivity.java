package com.test.instantsystem.lemonderss.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.test.instantsystem.lemonderss.R;
import com.test.instantsystem.lemonderss.adapters.NewsAdapter;
import com.test.instantsystem.lemonderss.fragments.NetworkFragment;
import com.test.instantsystem.lemonderss.interfaces.NetworkAccessCallback;
import com.test.instantsystem.lemonderss.models.News;
import com.test.instantsystem.lemonderss.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkAccessCallback
        , SwipeRefreshLayout.OnRefreshListener {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // UI objects
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // ArrayList is serializable and can be kept
    // easily in a bundle
    private ArrayList<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pull to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // Initialize recyclerView with empty newsList
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(this, newsList);
        mRecyclerView.setAdapter(mAdapter);
        // Use headless fragment to perform network operations
        // let activity handles UI part only
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), Constants.LE_MONDE_URL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // keep news list when activity is destroyed
        outState.putSerializable(Constants.NEWS_LIST_INTENT_TAG, newsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // recover newsList from previous activity
            newsList = (ArrayList<News>) savedInstanceState.getSerializable(Constants.NEWS_LIST_INTENT_TAG);
            // populate recycler view with newsList
            populateRecyclerView(newsList);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Callback used to pass newsList from async object
     *
     * @param result
     */
    @Override
    public void updateFromNetworkAccess(Object result) {
        // refresh is finished
        mSwipeRefreshLayout.setRefreshing(false);
        // occurs when an exception is thrown (Network, Parser)
        if (result == null) {
            // Generic message
            Toast.makeText(this, R.string.error_network, Toast.LENGTH_LONG).show();
            return;
        }
        newsList = (ArrayList<News>) result;
        // We replace the previous list with refreshed list.
        // In the case the list is very big, just add delta to the
        // already existing list.
        populateRecyclerView(newsList);
    }

    public void populateRecyclerView(List<News> newsList) {
        mAdapter.setNewsList(newsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishNetworkAccess() {
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onRefresh() {
        mNetworkFragment.startDownload();
    }
}
