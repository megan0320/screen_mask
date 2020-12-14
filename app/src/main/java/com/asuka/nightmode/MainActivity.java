package com.asuka.nightmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;


public class MainActivity extends Activity {

    private final static String LOG_TAG = "Night Mode" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent;

        if(Build.VERSION.SDK_INT >= 23){
            if(Settings.canDrawOverlays(this)){

                serviceIntent = new Intent(this, NightModeService.class);
                startService(serviceIntent);

            }else{
                try {
                    serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startService(serviceIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            serviceIntent = new Intent(this, NightModeService.class);
            startService(serviceIntent);
        }
        finish();
    }
}