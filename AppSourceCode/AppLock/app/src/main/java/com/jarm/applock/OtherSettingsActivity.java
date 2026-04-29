package com.jarm.applock;

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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class OtherSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (SettingsActivity.isAuthenticationSuccessful()) {
            setContentView(R.layout.other_settings);
        DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
        Switch disablesafemode = findViewById(R.id.disablesafemode);
        disablesafemode.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_SAFE_BOOT));
        disablesafemode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
            }
        });
        Switch disabledeveloperoptions = findViewById(R.id.disabledeveloperoptions);
            disabledeveloperoptions.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_DEBUGGING_FEATURES));
            disabledeveloperoptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_DEBUGGING_FEATURES);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_DEBUGGING_FEATURES);
            }
        });
        Switch disablefactoryreset = findViewById(R.id.disablefactoryreset);
            disablefactoryreset.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_FACTORY_RESET));
            disablefactoryreset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET);
            }
        });
        Switch disablenonemergencycalls = findViewById(R.id.disablenonemergencycalls);
            disablenonemergencycalls.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_OUTGOING_CALLS));
            disablenonemergencycalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS);
            }
        });
        Switch disablesms = findViewById(R.id.disablesms);
            disablesms.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_SMS));
            disablesms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_SMS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_SMS);
            }
        });
        Switch disablecreatenewandroiduseraccount = findViewById(R.id.disablecreatenewandroiduseraccount);
           disablecreatenewandroiduseraccount.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_ADD_USER));
           disablecreatenewandroiduseraccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_ADD_USER);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_ADD_USER);
            }
        });
            Switch disabledeleteandroiduseraccount = findViewById(R.id.disabledeleteandroiduseraccount);
            disabledeleteandroiduseraccount.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_REMOVE_USER));
            disabledeleteandroiduseraccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                    if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_REMOVE_USER);
                    else dpm.clearUserRestriction(admin, UserManager.DISALLOW_REMOVE_USER);
                }
            });
            Switch disableandroidaccountaddandremove = findViewById(R.id.disableandroidaccountaddandremove);
            disableandroidaccountaddandremove.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_MODIFY_ACCOUNTS));
            disableandroidaccountaddandremove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                    if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
                    else dpm.clearUserRestriction(admin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
                }
            });
        Switch disableappinstall = findViewById(R.id.disableappinstall);
            disableappinstall.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_INSTALL_APPS));
            disableappinstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_INSTALL_APPS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_INSTALL_APPS);
            }
        });
        Switch disableappuninstall = findViewById(R.id.disableappuninstall);
            disableappuninstall.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_UNINSTALL_APPS));
        disableappuninstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_UNINSTALL_APPS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_UNINSTALL_APPS);
            }
        });
        Switch disablewallpaperchange = findViewById(R.id.disablewallpaperchange);
            disablewallpaperchange.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_SET_WALLPAPER));
            disablewallpaperchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_SET_WALLPAPER);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_SET_WALLPAPER);
            }
        });
        Switch disableusbfiletransfers = findViewById(R.id.disableusbfiletransfers);
            disableusbfiletransfers.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_USB_FILE_TRANSFER));
            disableusbfiletransfers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER);
            }
        });
        Switch disablesystemwideprivatednschange = findViewById(R.id.disablesystemwideprivatednschange);
            disablesystemwideprivatednschange.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_CONFIG_PRIVATE_DNS));
            disablesystemwideprivatednschange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_PRIVATE_DNS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_PRIVATE_DNS);
            }
        });
        Switch disablesystemwidevpnchange = findViewById(R.id.disablesystemwidevpnchange);
            disablesystemwidevpnchange.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_CONFIG_VPN));
            disablesystemwidevpnchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_VPN);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_VPN);
            }
        });
        Switch disableusercredentialchange = findViewById(R.id.disableusercredentialchange);
        disableusercredentialchange.setChecked(dpm.getUserRestrictions(admin).getBoolean(UserManager.DISALLOW_CONFIG_CREDENTIALS));
        disableusercredentialchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b) dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_CREDENTIALS);
                else dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_CREDENTIALS);
            }
        });
            Button lockpasswordchangebutton = findViewById(R.id.lockpasswordchangebutton);
            lockpasswordchangebutton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), PasswordSetupActivity.class));
                }
            });
            Button removedeviceowner = findViewById(R.id.removedeviceowner);
            removedeviceowner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SettingsActivity.isAuthenticationSuccessful()) {
                        Toast.makeText(OtherSettingsActivity.this, "The device owner,app lock and app lock settings are being removed.Please wait", Toast.LENGTH_SHORT).show();
                        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                        //                   int userHandle = UserHandle.myUserId();
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
                        dpm.clearUserRestriction(admin, UserManager.DISALLOW_REMOVE_USER);
                        dpm.clearUserRestriction(admin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
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
                        Toast.makeText(OtherSettingsActivity.this, "The device owner,app lock and app lock settings have been removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                }
            });
    }
        else {
            finishAndRemoveTask();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        finishAndRemoveTask();
    }
    @Override
    public void onBackPressed() {
        stopLockTask();
        finishAndRemoveTask();
    }
}