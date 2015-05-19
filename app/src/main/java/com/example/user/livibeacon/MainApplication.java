package com.example.user.livibeacon;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.example.user.livibeacon.services.BeaconDetectionHelper;
import com.parse.Parse;

/**
 * Created by User on 2015/03/13.
 */
public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication instance;
    private BeaconManager beaconManager;
    private BeaconDetectionHelper beaconDetectionHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Parse.enableLocalDatastore(this);
        Log.i(TAG,"MAin App on Create was called");
        Parse.initialize(this, "QKaox8gwjnWA4KXq7wOqVDPwbnIHOUeGdNA7lV0x", "pfLiuCth3t6K21go0SErzqNtdL00uF1ZALw5AAde");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public MainApplication getInstance()
    {
        return instance;
    }

    public BeaconManager getBeaconManager()
    {
        return beaconManager;
    }
}
