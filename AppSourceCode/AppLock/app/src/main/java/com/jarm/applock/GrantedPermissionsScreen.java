package com.jarm.applock;

import android.accessibilityservice.AccessibilityService;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class GrantedPermissionsScreen extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.granted_permissions_screen);
        File issafemodequestionanswered = new File(getDataDir() + "/safemodequestionanswered");
        Button buttontoopenaccsettings = findViewById(R.id.buttontoopenaccsettings);
        buttontoopenaccsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            finishAndRemoveTask();
            }
        });
        Button cancelsetupinpermgrantscreen = findViewById(R.id.cancelsetupinpermgrantscreen);
        cancelsetupinpermgrantscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isThisAppDeviceOwner(getApplicationContext())) {
if (!PasswordHashing.isPasswordSet(getApplicationContext()) || AccSrvForBootstrapAndLock.tempunlockedpackagenames.contains("jarmapplock:authenticatedeviceownerremovalinsetup")) {
                    Toast.makeText(GrantedPermissionsScreen.this, "Cancelling setup and removing device owner.Please wait", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GrantedPermissionsScreen.this, "Setup cancelled and device owner removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
else {
    Intent i = new Intent(getApplicationContext(),AuthenticationActivity.class);
    i.putExtra("lockedapppackagename","jarmapplock:authenticatedeviceownerremovalinsetup");
    startActivity(i);
}
                } else {
                Toast.makeText(GrantedPermissionsScreen.this, "No change has been done to your device since this app was never set as device owner so you dont need to use this option.To cancel setup just uninstall this app", Toast.LENGTH_SHORT).show();
            finishAffinity();
                }
        }
        });
        Button closegrantedpermissionsscreen = findViewById(R.id.closegrantedpermissionsscreen);
        closegrantedpermissionsscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finishAndRemoveTask();
            }
        });
        int writesecuresettingspermission = checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS");
        int readlogspermission = checkSelfPermission("android.permission.READ_LOGS");
        Switch writesecuresettingspermissionstate = findViewById(R.id.writesecuresettingspermissionstate);
        Switch readlogspermissionstate = findViewById(R.id.readlogspermissionstate);
        Switch accessibilitypermissionstate = findViewById(R.id.accessibilitypermissionstate);
        Switch deviceownerpermissionstate = findViewById(R.id.deviceownerpermissionstate);


        if (isAccessibilityServiceEnabled(getApplicationContext(), AccSrvForBootstrapAndLock.class)) {
            accessibilitypermissionstate.setChecked(true);
        }
        else {
            accessibilitypermissionstate.setChecked(false);
            //if (!isAccessibilityServiceEnabled(context, MyAccessibilityService.class)) {
            //}
        }
        if (isThisAppDeviceOwner(getApplicationContext())) {
            deviceownerpermissionstate.setChecked(true);
        }
        else {
            deviceownerpermissionstate.setChecked(false);
        }
        if (readlogspermission == 0) {
            readlogspermissionstate.setChecked(true);
        }
        else {
            readlogspermissionstate.setChecked(false);
        }
        if (writesecuresettingspermission == 0) {
            writesecuresettingspermissionstate.setChecked(true);
        }
        else {
            writesecuresettingspermissionstate.setChecked(false);
        }
        if (readlogspermission == 0 && writesecuresettingspermission == 0 && isThisAppDeviceOwner(getApplicationContext()) && isAccessibilityServiceEnabled(getApplicationContext(), AccSrvForBootstrapAndLock.class)) {
           if (!issafemodequestionanswered.exists()) {
            setContentView(R.layout.safe_mode_disable_screen);
            Button nextbuttoninsafemodedisablescreen = findViewById(R.id.nextbuttoninsafemodedisablescreen);
            nextbuttoninsafemodedisablescreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!PasswordHashing.isPasswordSet(getApplicationContext())) {
                    CheckBox disablesafemodecheckbox = findViewById(R.id.disablesafemodecheckbox);
                    if (disablesafemodecheckbox.isChecked()) {
                        DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                        dpm.addUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                        try {
                            issafemodequestionanswered.createNewFile();
                        } catch (Exception e) {
                        }
                    } else {
                        DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(getApplicationContext(), DeviceAdminReceiverForDeviceOwner.class);
                        dpm.clearUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
                        try {
                            issafemodequestionanswered.createNewFile();
                        } catch (Exception e) {
                        }
                    }
                    setContentView(R.layout.setup_complete);
                    Button passwordsetbutton = findViewById(R.id.passwordsetbutton);
                    passwordsetbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), PasswordSetupActivity.class));
                            //finishAndRemoveTask();
                        }
                    });
                }
                    else {

                    }
            }
            });
        }
           else {
               setContentView(R.layout.setup_complete);
               Button passwordsetbutton = findViewById(R.id.passwordsetbutton);
               passwordsetbutton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       startActivity(new Intent(getApplicationContext(), PasswordSetupActivity.class));
                       //finishAndRemoveTask();
                   }
               });
           }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}