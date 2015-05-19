package com.example.user.livibeacon.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
//import android.graphics.Region;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.user.livibeacon.MainActivity;
import com.example.user.livibeacon.R;

import java.util.List;

/**
 * Created by User on 2015/03/20.
 */
public class BeaconDetectionService extends Service {
    private static final String TAG = "AJ";
    private static final String ALL_BEACONS_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region beaconRegion = new Region("regionId",ALL_BEACONS_UUID,null,null);
    private BeaconDetectionHelper detectionHelper;
    private BeaconManager beaconManager;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("AJ","On crete was called");
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        detectionHelper = BeaconDetectionHelper.getIntsance(BeaconDetectionService.this);
        beaconManager = detectionHelper.getBeaconManager();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(beaconRegion);

                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.i(TAG,"Coud not start Monitoring");
                }
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                try {
                    beaconManager.startRanging(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                int temp = -1000;
                int major = 0;
                int minor = 0;
                Beacon b = null;
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Ibeacon Detected")
                        .setContentText("There is a Ibeacon in your area")
                        .setSmallIcon(R.drawable.resource_icons_doc)
                        .setAutoCancel(true)
                        .build();

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0,notification);
                for (int x = 0; x < beacons.size(); x++) {
                    Log.i(TAG, "Monitoring Beacons Discovered " + beacons.get(x).getMajor() + " " + beacons.get(x).getMinor());

                    if(beacons.get(x).getRssi() > temp  && beacons.get(x).getRssi() != 0) {
                        temp = beacons.get(x).getRssi();
                        b = beacons.get(x);
                    }
                }

                if(b != null){
                    Intent i = new Intent("BROADCAST_SERVICE");
                    i.putExtra("major",b.getMajor());
                    i.putExtra("minor", b.getMinor());
                    i.putExtra("ris",b.getRssi());

                    localBroadcastManager.sendBroadcast(i);

                }
                else{
                    Log.i("AJ","Beacon B is null");
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                try {
                    beaconManager.stopRanging(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                int temp = -1000;
                Intent i = new Intent("BROADCAST_SERVICE");
                for(int x = 0; x < beacons.size();x++){
                    if(beacons.get(x).getRssi() > temp){
                        temp = beacons.get(x).getRssi();

                        i.putExtra("major", beacons.get(x).getMajor());
                        i.putExtra("minor", beacons.get(x).getMinor());
                        i.putExtra("ris",beacons.get(x).getRssi());
                    }
                    Log.i("AJ","Ranging Major: " + beacons.get(x).getMajor() + " Minor: " + beacons.get(x).getMinor());
//                    Intent i = new Intent("BROADCAST_SERVICE");
//                    i.putExtra("major", beacons.get(x).getMajor());
//                    i.putExtra("minor", beacons.get(x).getMinor());


                }
                localBroadcastManager.sendBroadcast(i);
            }
        });

        beaconManager.setForegroundScanPeriod(20000,10000);

        return START_STICKY;


    }






}
