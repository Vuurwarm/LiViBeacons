package com.example.user.livibeacon;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.user.livibeacon.activities.ChillRoom;
import com.example.user.livibeacon.activities.EnginRoom;
import com.example.user.livibeacon.activities.KoffeeShop;
import com.example.user.livibeacon.services.BeaconDetectionHelper;
import com.example.user.livibeacon.services.BeaconDetectionService;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;


public class MainActivity extends Activity {
    private final String TAG ="MAINACTIVITY";
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region region = new Region("regionID",ESTIMOTE_PROXIMITY_UUID,null,null);
    private  BeaconManager beaconManager;
    private TextView major;
    private TextView minor;
    private TextView ris;
    private BeaconDetectionHelper beaconDetectionHelper;

    private BroadcastReceiver reciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("AJ","WE GOT SOMTHING NOW LETS DO SHIT");
            major.setText(String.valueOf(intent.getIntExtra("major",0)));
            minor.setText(String.valueOf(intent.getIntExtra("minor", 0)));
            ris.setText(String.valueOf(intent.getIntExtra("ris", 0)));
            Intent i = null;

            int major = intent.getIntExtra("major",0);
            switch(major){
                case 57712 :
                   // i = new Intent(MainActivity.this, KoffeeShop.class);

                    break;
                case 45286 :
                    //i = new Intent(MainActivity.this, KoffeeShop.class);
                    break;
                case 65440 :
                    //i = new Intent(MainActivity.this, KoffeeShop.class);
                    break;
            }
            //startActivity(i);
            
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainApplication mainApp = (MainApplication)getApplication();
        major  = (TextView)findViewById(R.id.major);
        minor = (TextView)findViewById(R.id.minor);
        ris = (TextView)findViewById(R.id.ris);

        beaconDetectionHelper = BeaconDetectionHelper.getIntsance(getApplicationContext());
        beaconManager = beaconDetectionHelper.getBeaconManager();

        //We sheck if there are any services running and if not we
        //start the BeaconDetection Services
        if(!isDetectionRunning())
        {
            Intent startBeaconIntent = new Intent(getApplicationContext(), BeaconDetectionService.class);
            startService(startBeaconIntent);
        }



        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(reciver, new IntentFilter("BROADCAST_SERVICE"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager.startMonitoring(region);
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
            beaconManager.startMonitoring(region);
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
