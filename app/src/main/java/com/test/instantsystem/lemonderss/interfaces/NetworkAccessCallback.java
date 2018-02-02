package com.test.instantsystem.lemonderss.interfaces;

import android.net.NetworkInfo;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */

public interface NetworkAccessCallback<T> {

    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromNetworkAccess(T result);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishNetworkAccess();
}
