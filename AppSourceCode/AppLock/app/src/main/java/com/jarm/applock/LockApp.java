package com.jarm.applock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import androidx.navigation.ui.AppBarConfiguration;

public class LockApp extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DevicePolicyManager dpm;
        ComponentName admin;
        String lockedapppackagename = "null";
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        admin = new ComponentName(this, DeviceAdminReceiverForDeviceOwner.class);
        try  {
            startLockTask();
            stopLockTask();
        }
        catch (Exception e) {
        }
        super.onCreate(savedInstanceState);
        try {
            stopLockTask();
        } catch (Exception e) {
        }
        Handler j = new Handler();
        j.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLockTask();
            }
        },1000);
            stopLockTask();
            stopLockTask();
            stopLockTask();
            stopLockTask();
            stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        stopLockTask();
        if (getIntent().getExtras() != null) {
            lockedapppackagename = getIntent().getExtras().getString("lockedapppackagename");
        }
Intent authenticationactivitylaunchparameters = new Intent(this,AuthenticationActivity.class);
authenticationactivitylaunchparameters.putExtra("lockedapppackagename",lockedapppackagename);
startActivity(authenticationactivitylaunchparameters);
    }
    @Override
    public void onBackPressed() {
        stopLockTask();
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setPackage(getApplicationContext().getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY).get(0).activityInfo.packageName));
    }
    @Override
    protected void onResume() {
        super.onResume();
     //finishAndRemoveTask();
        //finishAndRemoveTask();
        //finishAndRemoveTask();
        //finishAndRemoveTask();
        //finishAndRemoveTask();
    }
}