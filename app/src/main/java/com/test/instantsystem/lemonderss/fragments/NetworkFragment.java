package com.test.instantsystem.lemonderss.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.test.instantsystem.lemonderss.interfaces.NetworkAccessCallback;
import com.test.instantsystem.lemonderss.remote.DownloadXmlTask;
import com.test.instantsystem.lemonderss.utils.Constants;

public class NetworkFragment extends Fragment {

    // callback object to pass data to main activity
    private NetworkAccessCallback mCallback;
    private String mUrlString;
    private DownloadXmlTask mDownloadXmlTask;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(Constants.NETWORK_INTENT_TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            args.putString(Constants.URL_INTENT_TAG, url);
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, Constants.NETWORK_INTENT_TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrlString = getArguments().getString(Constants.URL_INTENT_TAG);
        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);
        // download rss stream
        startDownload();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (NetworkAccessCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;
    }

    /**
     * Start non-blocking execution of DownloadXmlTask.
     */
    public void startDownload() {
        cancelDownload();
        mDownloadXmlTask = new DownloadXmlTask(mCallback);
        mDownloadXmlTask.execute(mUrlString);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadXmlTask != null) {
            mDownloadXmlTask.cancel(true);
        }
    }
}
