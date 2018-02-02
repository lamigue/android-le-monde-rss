package com.test.instantsystem.lemonderss.remote;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.test.instantsystem.lemonderss.interfaces.NetworkAccessCallback;
import com.test.instantsystem.lemonderss.models.News;
import com.test.instantsystem.lemonderss.utils.Constants;
import com.test.instantsystem.lemonderss.utils.LeMondeXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */

public class DownloadXmlTask extends AsyncTask<String, Void, List<News>> {

    private NetworkAccessCallback mCallback;

    public DownloadXmlTask(NetworkAccessCallback callback) {
        mCallback = callback;
    }

    /**
     * Cancel background network operation if we do not have network connectivity.
     */
    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update callback with null data.
                mCallback.updateFromNetworkAccess(null);
                cancel(true);
            }
        }
    }

    @Override
    protected List<News> doInBackground(String... urls) {
        List<News> newsList = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                newsList = loadXmlFromNetwork(urls[0]);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newsList;
    }

    @Override
    protected void onPostExecute(List<News> newsList) {
        if (mCallback != null) {
            // update activity with newsList
            mCallback.updateFromNetworkAccess(newsList);
            mCallback.finishNetworkAccess();
        }
    }

    /**
     * Uploads XML from le Monde, parses it, and combines it with
     * HTML markup. Returns HTML string.
     * @param urlString
     * @return List of news
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<News> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        List<News> newsList = null;

        stream = downloadUrl(urlString);
        newsList = LeMondeXmlParser.parse(stream);
        if (stream != null) {
            stream.close();
        }

        return newsList;
    }

    /**
     * Given a string representation of an Url, sets up a connection and gets
     * an input stream.
     *
     * @param urlString
     * @return InputStream
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream stream = null;
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(Constants.NETWORK_TIMEOUT);
        conn.setConnectTimeout(Constants.NETWORK_TIMEOUT);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        // Retrieve the response body as an InputStream.
        stream = conn.getInputStream();
        return stream;
    }
}
