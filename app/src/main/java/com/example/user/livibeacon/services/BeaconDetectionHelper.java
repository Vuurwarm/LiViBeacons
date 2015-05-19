package com.example.user.livibeacon.services;

import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.BeaconManager;

/**
 * Created by User on 2015/03/20.
 */
public class BeaconDetectionHelper {
    private static BeaconDetectionHelper instance;
    private BeaconManager beaconManager;

    private Context context;

    public BeaconDetectionHelper(Context context)
    {
        this.context = context;
        beaconManager = new BeaconManager(context);
    }

    public static BeaconDetectionHelper getIntsance(Context context)
    {
        if(instance == null)
        {
            instance = new BeaconDetectionHelper(context);
        }

        return instance;

    }

    public BeaconManager getBeaconManager()
    {
        return beaconManager;
    }

    public void startRanging()
    {
        //Intent i = new Intent(this, BeaconDetectionService.);
    }
}
