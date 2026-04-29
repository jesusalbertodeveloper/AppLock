package com.jarm.applock;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogCatMonitorService extends Service {
    private HandlerThread handlerThread;
    private Handler serviceHandler;

    private boolean receivedbootcompletedbroadcast = false;

    private ArrayList<String> lockedapps = new ArrayList<String>();
    public boolean shouldprocessBroadcast() {
        if (AccSrvForBootstrapAndLock.getInstance().uptimeMins < 3) {
            return receivedbootcompletedbroadcast;
        }
        else {
            return true;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        String KEY_TO_WATCH = "enabled_accessibility_services";

        Uri uri = Settings.Secure.getUriFor(KEY_TO_WATCH);
        ContentObserver mSecureObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String accessibilityservice = Settings.Secure.getString(getContentResolver(),"enabled_accessibility_services");
                if (accessibilityservice == null || accessibilityservice.isEmpty()) {
                    accessibilityservice = "com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock";
                    Settings.Secure.putString(getContentResolver(),"enabled_accessibility_services",accessibilityservice);
                    startActivity(new Intent(getApplicationContext(), AccServDisableError.class));
                }
                else if (!accessibilityservice.contains("com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock")) {
                    accessibilityservice = accessibilityservice + ";com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock";
                    Settings.Secure.putString(getContentResolver(),"enabled_accessibility_services",accessibilityservice);
                startActivity(new Intent(getApplicationContext(), AccServDisableError.class));
                }
            }
        };
        getContentResolver().registerContentObserver(uri, false, mSecureObserver);
        Uri uri1 = Settings.Secure.getUriFor("applock_emergency_remove");
        ContentObserver mSecureObserver1 = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                try {
                    int accessibilityservice = Settings.Secure.getInt(getContentResolver(), "applock_emergency_remove");
                    if (accessibilityservice == 1) {
                        Toast.makeText(LogCatMonitorService.this, "Removing app lock and device owner.Please wait...", Toast.LENGTH_SHORT).show();
                        Settings.Secure.putInt(getContentResolver(), "applock_emergency_remove", 0);
                        //startActivity(new Intent(getApplicationContext(), AccServDisableError.class));
                        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                        //                   int userHandle = UserHandle.myUserId();
                        List<ApplicationInfo> applist = getPackageManager().getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES|PackageManager.MATCH_DISABLED_COMPONENTS
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
                        applocksettingsfile.delete();
                        encryptedpasswordhashfile.delete();
                        Toast.makeText(LogCatMonitorService.this, "Device owner and app lock removed.You can now reconfigure or uninstall the app lock", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
};
getContentResolver().registerContentObserver(uri1, false, mSecureObserver1);
        Uri uri2 = Settings.Secure.getUriFor("applock_safe_mode");
        ContentObserver mSecureObserver2 = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                try {
                    int accessibilityservice = Settings.Secure.getInt(getContentResolver(), "applock_safe_mode");
                    if (accessibilityservice == 2) {
                        Settings.Secure.putInt(getContentResolver(), "applock_safe_mode", 0);
                        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
dpm.addUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                        //startActivity(new Intent(getApplicationContext(), AccServDisableError.class));
                    }
                    else if (accessibilityservice == 1) {
                            Settings.Secure.putInt(getContentResolver(), "applock_safe_mode", 0);
                            DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                            ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                            //startActivity(new Intent(getApplicationContext(), AccServDisableError.class));
                    }
                    else {
                        Settings.Secure.putInt(getContentResolver(), "applock_safe_mode", 0);
                    }
                } catch (Exception e) {
                }
            }
        };
        getContentResolver().registerContentObserver(uri2, false, mSecureObserver2);
        Uri uri3 = Settings.Secure.getUriFor("applock_password_reset");
        ContentObserver mSecureObserver3 = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                try {
                    int accessibilityservice = Settings.Secure.getInt(getContentResolver(), "applock_password_reset");
                    if (accessibilityservice == 1) {
                        Settings.Secure.putInt(getContentResolver(), "applock_password_reset", 0);
                        startActivity(new Intent(getApplicationContext(), PasswordSetupActivity.class));
                    }
                } catch (Exception e) {
                }
            }
        };
getContentResolver().registerContentObserver(uri3, false, mSecureObserver3);
        handlerThread = new HandlerThread("MyServiceThread");
        handlerThread.start();
        serviceHandler = new Handler(handlerThread.getLooper());
    }
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            serviceHandler.post(() -> {
                Process process = null;
                BufferedReader reader = null;

                try {
                    process = new ProcessBuilder("logcat" ," ActivityManager:I")
                            .redirectErrorStream(true)
                            .start();

                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != "00") {
                        AccSrvForBootstrapAndLock.getInstance().uptimeMins = TimeUnit.MILLISECONDS.toMinutes(SystemClock.elapsedRealtime());
                        if (line.contains("ActivityManager") && line.contains("Finished processing BOOT_COMPLETED")) {
                            receivedbootcompletedbroadcast = true;
                        }
                        //The app launch is detected by reading the Android logcat as that's faster than normal Android APIs
                        //The word killing is filtered out to avoid triggering the applock screen when the app is killed(e.g a force stop)
                        //The words Force stopping and Force killing are also filtered out to avoid triggering the applock screen whenever the app is force stopped
                        //It is important to keep in mind that the Android system logcat entries are always written in English even if the device is configured to use a different display language so no localization needed here
                        if (line != null && line.contains("ActivityManager") && !line.contains("Killing") && !line.contains("Force stopping") && !line.contains("Force finishing") && !line.contains("InstalledAppDetails") && !line.contains("Unable to start")) {
                            if (!AuthenticationActivity.alreadyrunning && AccSrvForBootstrapAndLock.getInstance().applocksettingss.isAppPackageNameLocked(line)) {
                               if (!line.contains("for broadcast")) {
                                   try {
                                       AccSrvForBootstrapAndLock.getInstance().lockTask(AccSrvForBootstrapAndLock.getInstance().applocksettingss.findPackageNameFromLogEntry(line));
                                   } catch (Exception e) {
                                   }
                               }
                                else if (line.contains("for broadcast") && shouldprocessBroadcast()) {
                                    try {
                                        AccSrvForBootstrapAndLock.getInstance().lockTask(AccSrvForBootstrapAndLock.getInstance().applocksettingss.findPackageNameFromLogEntry(line));
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
        return START_STICKY;
}
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
        @Override
        public void onDestroy() {
            handlerThread.quitSafely();
            super.onDestroy();
        }
    }