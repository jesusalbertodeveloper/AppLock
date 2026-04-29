package com.jarm.applock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SetupActivity extends AppCompatActivity {

    public boolean isThisAppDeviceOwner(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        String packageName = context.getPackageName();
        return dpm.isDeviceOwnerApp(packageName);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(getApplicationContext(),PasswordSetupActivity.class));
        File isprivacypolicyaccepted = new File(getDataDir() + "/acceptprivacypolicy");
        File isdisclaimeraccepted = new File(getDataDir() + "/acceptdisclaimer");
        if (!isprivacypolicyaccepted.exists()) {
            setContentView(R.layout.privacy_policy);
            Button disagreebutton = findViewById(R.id.disagreebutton);
            disagreebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               setContentView(R.layout.privacy_policy_rejected_screen);
              Button button922999 = findViewById(R.id.button922999);
              button922999.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      finishAffinity();
                  }
              });
                }
            });
            Button agreebutton = findViewById(R.id.agreebutton);
            agreebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        isprivacypolicyaccepted.createNewFile();
                    } catch (Exception e) {
                    }
                    setContentView(R.layout.disclaimer_screen);
                    Button disagreebutton1 = findViewById(R.id.disagreebutton1);
                    disagreebutton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setContentView(R.layout.disclaimer_rejected_screen);
                            Button button922933 = findViewById(R.id.button922933);
                            button922933.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishAffinity();
                                }
                            });
                        }
                    });
                    Button agreebutton1 = findViewById(R.id.agreebutton1);
                    agreebutton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setContentView(R.layout.permission_screen);
                            Button buttontoopenaccsettings = findViewById(R.id.firstaccsettingsshortcutinappplock);
                            buttontoopenaccsettings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(intent);
                                }
                            });
                            Button cancelsetupinpermgrantscreen = findViewById(R.id.firstcancelsetupofapplockbutton);
                            cancelsetupinpermgrantscreen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isThisAppDeviceOwner(getApplicationContext())) {
                                        if (!PasswordHashing.isPasswordSet(getApplicationContext()) || AccSrvForBootstrapAndLock.tempunlockedpackagenames.contains("jarmapplock:authenticatedeviceownerremovalinsetup")) {
                                            Toast.makeText(SetupActivity.this, "Cancelling setup and removing device owner.Please wait", Toast.LENGTH_SHORT).show();
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
                                            applocksettingsfile.delete();
                                            encryptedpasswordhashfile.delete();
                                            File acceptdisclaimer = new File(getDataDir() + "/acceptdisclaimer");
                                            File acceptprivacypolicy = new File(getDataDir() + "/acceptprivacypolicy");
                                            acceptdisclaimer.delete();
                                            acceptprivacypolicy.delete();
                                            Toast.makeText(SetupActivity.this, "Setup cancelled and device owner removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                        }
                                        else {
                                            Intent i = new Intent(getApplicationContext(),AuthenticationActivity.class);
                                            i.putExtra("lockedapppackagename","jarmapplock:authenticatedeviceownerremovalinsetup");
                                            startActivity(i);
                                        }
                                    } else {
                                        Toast.makeText(SetupActivity.this, "No change has been done to your device since this app was never set as device owner so you dont need to use this option.To cancel setup just uninstall this app", Toast.LENGTH_SHORT).show();
                                        finishAffinity();
                                    }
                                }
                            });
                            Button permissioncheck = findViewById(R.id.permissioncheck);
                            permissioncheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //startActivity(new Intent(getApplicationContext(),GrantedPermissionsScreen.class));
//startActivity(new Intent(getApplicationContext(),));
                                    startActivity(new Intent(getApplicationContext(), GrantedPermissionsScreen.class));
                                    //setContentView(R.layout.permission_screen);
                                }
                            });
                        }
                    });
                }
            });
        }
    else if (!isdisclaimeraccepted.exists()){
            setContentView(R.layout.disclaimer_screen);
            Button disagreebutton1 = findViewById(R.id.disagreebutton1);
            disagreebutton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
setContentView(R.layout.disclaimer_rejected_screen);
                    Button button922933 = findViewById(R.id.button922933);
                    button922933.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishAffinity();
                        }
                    });
                }
            });
            Button agreebutton1 = findViewById(R.id.agreebutton1);
            agreebutton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SetupActivity.this, "AGREE", Toast.LENGTH_SHORT).show();
                    try {
                        isdisclaimeraccepted.createNewFile();
                    } catch (Exception e) {
                    }
                    setContentView(R.layout.permission_screen);
                    Button buttontoopenaccsettings = findViewById(R.id.firstaccsettingsshortcutinappplock);
                    buttontoopenaccsettings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    });
                    Button cancelsetupinpermgrantscreen = findViewById(R.id.firstcancelsetupofapplockbutton);
                    cancelsetupinpermgrantscreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isThisAppDeviceOwner(getApplicationContext())) {
                                if (!PasswordHashing.isPasswordSet(getApplicationContext()) || AccSrvForBootstrapAndLock.tempunlockedpackagenames.contains("jarmapplock:authenticatedeviceownerremovalinsetup")) {
                                    Toast.makeText(SetupActivity.this, "Cancelling setup and removing device owner.Please wait", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SetupActivity.this, "Setup cancelled and device owner removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                }
                                else {
                                    Intent i = new Intent(getApplicationContext(),AuthenticationActivity.class);
                                    i.putExtra("lockedapppackagename","jarmapplock:authenticatedeviceownerremovalinsetup");
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(SetupActivity.this, "No change has been done to your device since this app was never set as device owner so you dont need to use this option.To cancel setup just uninstall this app", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                            }
                        }
                    });
                    Button permissioncheck = findViewById(R.id.permissioncheck);
                    permissioncheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //startActivity(new Intent(getApplicationContext(),GrantedPermissionsScreen.class));
//startActivity(new Intent(getApplicationContext(),));
                            startActivity(new Intent(getApplicationContext(), GrantedPermissionsScreen.class));
                            //setContentView(R.layout.permission_screen);
                        }
                    });
                }
            });
}
    else {
            setContentView(R.layout.permission_screen);
            Button buttontoopenaccsettings = findViewById(R.id.firstaccsettingsshortcutinappplock);
            buttontoopenaccsettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            });
            Button cancelsetupinpermgrantscreen = findViewById(R.id.firstcancelsetupofapplockbutton);
            cancelsetupinpermgrantscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isThisAppDeviceOwner(getApplicationContext())) {
                        if (!PasswordHashing.isPasswordSet(getApplicationContext()) || AccSrvForBootstrapAndLock.tempunlockedpackagenames.contains("jarmapplock:authenticatedeviceownerremovalinsetup")) {
                            Toast.makeText(SetupActivity.this, "Cancelling setup and removing device owner.Please wait", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(SetupActivity.this, "Setup cancelled and device owner removed.You can now uninstall this app", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                        else {
                            Intent i = new Intent(getApplicationContext(),AuthenticationActivity.class);
                            i.putExtra("lockedapppackagename","jarmapplock:authenticatedeviceownerremovalinsetup");
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(SetupActivity.this, "No change has been done to your device since this app was never set as device owner so you dont need to use this option.To cancel setup just uninstall this app", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                }
            });
            Button permissioncheck = findViewById(R.id.permissioncheck);
            permissioncheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(getApplicationContext(),GrantedPermissionsScreen.class));
//startActivity(new Intent(getApplicationContext(),));
                    startActivity(new Intent(getApplicationContext(), GrantedPermissionsScreen.class));
                    //setContentView(R.layout.permission_screen);
                }
            });
        }
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    }
