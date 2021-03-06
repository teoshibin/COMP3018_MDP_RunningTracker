package com.lucasteo.runningtracker.service;

/**
 * service callback
 */
public interface ICallback {

    /**
     * directly pass user stop moving status update to user interface
     *
     * @param value stop moving boolean
     */
    void onStopMovingUpdateEvent(boolean value);

    /**
     * called when permission needed in service is not granted
     */
    void onPermissionNotGranted();
}
