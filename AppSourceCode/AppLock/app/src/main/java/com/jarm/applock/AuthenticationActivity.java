package com.jarm.applock;

import static com.jarm.applock.PasswordHashing.verifyPassword;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

    static public boolean alreadyrunning = false;
    int failedattempts = 0;
    long startTimeMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alreadyrunning = true;
        setContentView(R.layout.authentication_activity);
        String lockedapppackagename = getIntent().getExtras().getString("lockedapppackagename");
        if (lockedapppackagename == null) {
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setPackage(getApplicationContext().getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY).get(0).activityInfo.packageName));
        }
        if (lockedapppackagename.equals("jarmapplock:authenticatedeviceownerremovalinsetup"))
        {
            TextView textt = findViewById(R.id.textView839);
            textt.setText("Removing this app's device owner and settings is locked with a password.Please enter said password below:");
            Toolbar atoolbar = findViewById(R.id.toolbar284);
            atoolbar.setTitle("Remove device owner and app lock settings");
        }
        Button unlockbutton = findViewById(R.id.unlockbutton);
        unlockbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordfield = findViewById(R.id.passwordinputfieldforauthentication);
                boolean ok = verifyPassword(getApplicationContext(), passwordfield.getText().toString());
                if (failedattempts == 5 && startTimeMillis != 0 && System.currentTimeMillis() - startTimeMillis >= 30_000) {
                    failedattempts = 0;
                    startTimeMillis = 0;
                }
                if (failedattempts == 5 && startTimeMillis != 0 && System.currentTimeMillis() - startTimeMillis < 30_000) {
                          unlockbutton.setText("Wait 30 seconds before trying again");
                } else if (ok && lockedapppackagename != null && !lockedapppackagename.equals("null")) {
                    failedattempts = 0;
                    startTimeMillis = 0;
                    AccSrvForBootstrapAndLock.tempunlockedpackagenames.add(lockedapppackagename);
                    if (!lockedapppackagename.equals("jarmapplock:authenticatedeviceownerremovalinsetup")) {
                        startActivity(getPackageManager().getLaunchIntentForPackage(lockedapppackagename));
                    }
                    else {
                                Toast.makeText(AuthenticationActivity.this, "Cancelling setup and removing device owner.Please wait", Toast.LENGTH_SHORT).show();
                                DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                                ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                                List<ApplicationInfo> applist = getPackageManager().getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.MATCH_DISABLED_COMPONENTS
                                        | PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS);
                                for (ApplicationInfo applisting : applist) {
                                    try {
                                        if (dpm.isPackageSuspended(admin, applisting.packageName)) {
                                            dpm.setPackagesSuspended(admin, new String[]{applisting.packageName}, false);
                                        }
                                    } catch (Exception e) {
                                    }
                                    try {
                                        if (dpm.isApplicationHidden(admin, applisting.packageName)) {
                                            dpm.setApplicationHidden(admin, applisting.packageName, false);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_DEBUGGING_FEATURES);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_SMS);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_ADD_USER);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_INSTALL_APPS);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_UNINSTALL_APPS);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_SET_WALLPAPER);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_PRIVATE_DNS);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_VPN);
                                dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_CREDENTIALS);
                                dpm.clearDeviceOwnerApp(getPackageName());
                                File applocksettingsfile = new File(getDataDir() + "/dataa");
                                File encryptedpasswordhashfile = new File(getDataDir() + "/shared_prefs/secure_prefs.xml");
                        File accpermgranted = new File(getDataDir() + "/accpermgranted");
                        File safemodequestionanswered = new File(getDataDir() + "/safemodequestionanswered");
                        applocksettingsfile.delete();
                        encryptedpasswordhashfile.delete();
                        accpermgranted.delete();
                        safemodequestionanswered.delete();
                        File acceptdisclaimer = new File(getDataDir() + "/acceptdisclaimer");
                        File acceptprivacypolicy = new File(getDataDir() + "/acceptprivacypolicy");
                        acceptdisclaimer.delete();
                        acceptprivacypolicy.delete();
                                Toast.makeText(AuthenticationActivity.this, "Setup cancelled and device owner removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                    }
                    } else if (failedattempts < 5) {
               unlockbutton.setText("Wrong password!");
                failedattempts++;
                } else {
                    unlockbutton.setText("Wrong password!");
                    startTimeMillis = System.currentTimeMillis();
                }
            }
            });
    }
    @Override
    public void onBackPressed() {
        alreadyrunning = false;
        stopLockTask();
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setPackage(getApplicationContext().getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY).get(0).activityInfo.packageName));
        finishAffinity();
        finishAndRemoveTask();
    }
    @Override
    protected void onPause() {
        super.onPause();
        alreadyrunning = false;
    stopLockTask();
            finishAffinity();
            finishAndRemoveTask();
    }
}