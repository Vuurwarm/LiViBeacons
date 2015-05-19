package com.example.user.livibeacon.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.user.livibeacon.R;
import com.example.user.livibeacon.services.BeaconDetectionHelper;
import com.example.user.livibeacon.services.BeaconDetectionService;

import java.util.List;

public class KoffeeShop extends ActionBarActivity {
    BeaconManager beaconManager;
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region region = new Region("regionID",ESTIMOTE_PROXIMITY_UUID,null,null);
    private BeaconDetectionHelper beaconDetectionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koffee_shop);
        beaconDetectionHelper = BeaconDetectionHelper.getIntsance(getApplicationContext());
        beaconManager = beaconDetectionHelper.getBeaconManager();
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.i("AJ","We are Ranging :D");
            }
        });

        if(!isDetectionRunning())
        {
            Intent startBeaconIntent = new Intent(getApplicationContext(), BeaconDetectionService.class);
            startService(startBeaconIntent);
        }



        //LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(reciver, new IntentFilter("BROADCAST_SERVICE"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager.startRanging(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            beaconManager.stopRanging(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    public boolean isDetectionRunning()
    {
        Class<?> serviceClass = BeaconDetectionService.class;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
