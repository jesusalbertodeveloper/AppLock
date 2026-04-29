package com.jarm.applock;

import static com.jarm.applock.PasswordHashing.verifyPassword;

import android.accessibilityservice.AccessibilityService;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.navigation.ui.AppBarConfiguration;

import com.jarm.applock.databinding.SettingsActivityBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    int failedattempts = 0;
    long startTimeMillis = 0;

    private static boolean authenticationsuccessful = false;

    private AppBarConfiguration appBarConfiguration;
    private SettingsActivityBinding binding;
    public static void logoutFromSettings() {
        authenticationsuccessful = false;
    }
    public boolean isThisAppDeviceOwner(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        String packageName = context.getPackageName();
        return dpm.isDeviceOwnerApp(packageName);
    }
    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> serviceClass) {
        ComponentName service = new ComponentName(context, serviceClass);
        String enabledServices = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServices == null) return false;

        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(enabledServices);
        while (splitter.hasNext()) {
            String componentNameString = splitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);
            if (enabledService != null && enabledService.equals(service)) {
                return true;
            }
        }
        return false;
    }
    private boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 || isUpdatedSystemApp(appInfo);
    }

    private boolean isUpdatedSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
    }
    public static boolean isAuthenticationSuccessful() {
        return authenticationsuccessful;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int writesecuresettingspermission = checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS");
        int readlogspermission = checkSelfPermission("android.permission.READ_LOGS");
        File issafemodequestionanswered = new File(getDataDir() + "/safemodequestionanswered");
        DevicePolicyManager dpm1 = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin1 = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
        File isaccessibilitypermissiongranted = new File(getDataDir() + "/accpermgranted");
        if (isaccessibilitypermissiongranted.exists() && checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == 0) {
            String accessibilityservice = Settings.Secure.getString(getContentResolver(), "enabled_accessibility_services");
            if (accessibilityservice == null || accessibilityservice.isEmpty()) {
                accessibilityservice = "com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock";
                Settings.Secure.putString(getContentResolver(),"enabled_accessibility_services",accessibilityservice);
            } else if (!accessibilityservice.contains("com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock")) {
                accessibilityservice = accessibilityservice + ";com.jarm.applock/com.jarm.applock.AccSrvForBootstrapAndLock";
                Settings.Secure.putString(getContentResolver(),"enabled_accessibility_services",accessibilityservice);
            }
        }
        if (!PasswordHashing.isPasswordSet(getApplicationContext()) || !issafemodequestionanswered.exists() || !isAccessibilityServiceEnabled(getApplicationContext(), AccSrvForBootstrapAndLock.class) || !isThisAppDeviceOwner(getApplicationContext()) || readlogspermission != 0 || writesecuresettingspermission != 0) {
            startActivity(new Intent(getApplicationContext(), SetupActivity.class));
            //finishAndRemoveTask();
            //return;
        }
else {
        //DevicePolicyManager dpm1 = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        //ComponentName admin1 = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
        dpm1.setLockTaskPackages(admin1, new String[]{"com.jarm.applock"});
//dpm1.clearUserRestriction(admin1, UserManager.DISALLOW_MODIFY_ACCOUNTS);
        File checkifapasswordhashfileexists = new File(getDataDir() + "/shared_prefs/secure_prefs.xml");
        if (!checkifapasswordhashfileexists.exists()) {
            startActivity(new Intent(getApplicationContext(), SetupActivity.class));
        }
        setContentView(R.layout.authentication_activity);
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
                } else if (ok) {
                    failedattempts = 0;
                    startTimeMillis = 0;
                    authenticationsuccessful = true;
                    DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                    ListView l;
                    setContentView(R.layout.settings_activity);
                    Button othersettings = findViewById(R.id.othersettings);
                    othersettings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), OtherSettingsActivity.class));
                        }
                    });
                    l = findViewById(R.id.list);
                    ArrayList<AppListItemForAppLockSettings> items;
                    ListViewAdapterForSettings adapter;
                    List<ApplicationInfo> applist = getPackageManager().getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.MATCH_DISABLED_COMPONENTS
                            | PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS);
                    AppLockSettings settingss = new AppLockSettings();
                    try {
                        FileInputStream file = new FileInputStream(getDataDir() + "/dataa");
                        ObjectInputStream in = new ObjectInputStream(file);
                        settingss = (AppLockSettings) in.readObject();
                        in.close();
                        file.close();
                        System.out.println("Object has been deserialized\nData after Deserialization.");
                    } catch (Exception exception) {
                        System.out.println("IOException is caught");
                        if (!new File(getDataDir() + "/dataa").exists()) {
                            try {
                                FileOutputStream file = new FileOutputStream(getDataDir() + "/dataa");
                                ObjectOutputStream out = new ObjectOutputStream(file);
                                out.writeObject(new AppLockSettings());
                                out.close();
                                file.close();
                                System.out.println("Object has been serialized\nData before Deserialization.");
                            } catch (Exception exceptionn) {
                            }
                        }
                    }
                    items = new ArrayList<>();
                    for (ApplicationInfo applisting : applist) {
                        try {
                            items.add(new AppListItemForAppLockSettings(applisting.packageName, applisting.loadLabel(getPackageManager()).toString(), settingss.isAppPackageNameLocked(applisting.packageName), dpm.isPackageSuspended(admin, applisting.packageName), dpm.isApplicationHidden(admin, applisting.packageName), isSystemApp(applisting)));
                        } catch (Exception e) {
                        }
                    }
                    adapter = new ListViewAdapterForSettings(getApplicationContext(), items);
                    l.setAdapter(adapter);
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
    }
    @Override
    public void onPause() {
        super.onPause();
        //logoutFromSettings();
        //finishAndRemoveTask();
    }
    @Override
    public void onBackPressed() {
logoutFromSettings();
stopLockTask();
        finishAffinity();
        finishAndRemoveTask();
    }
}