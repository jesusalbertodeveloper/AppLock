package com.jarm.applock;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.app.admin.DevicePolicyManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AccSrvForBootstrapAndLock extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";

    public long uptimeMins = TimeUnit.MILLISECONDS.toMinutes(SystemClock.elapsedRealtime());
    public static AppLockSettings applocksettingss = new AppLockSettings();

    public static ArrayList<String> tempunlockedpackagenames = new ArrayList<String>();

    private static AccSrvForBootstrapAndLock instance;
    private static boolean blockadditionofnewadbhosts = true;

    public static boolean isNewAdbHostAdditionBlocked() {
        return blockadditionofnewadbhosts;
    }

    public static void setAdbHostAdditionBlocked(boolean value) {
        blockadditionofnewadbhosts = value;
    }

    public static AccSrvForBootstrapAndLock getInstance() {
        return instance;
    }

    private String findPackageNameByFriendlyAppName(String friendlyAppName) {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        String packageName = "null";

        for (ApplicationInfo appInfo : apps) {
            try {
                String label = pm.getApplicationLabel(appInfo).toString();
                if (label.equalsIgnoreCase(friendlyAppName)) {
                    packageName = appInfo.packageName;
                    break;
                }
            } catch (Exception e) {
            }
        }
        return packageName;
    }
    private String findTextInNode(AccessibilityNodeInfo node) {
        if (node == null) return null;

        CharSequence text = node.getText();
        if (text != null && text.length() > 0) {
            return text.toString();
        }

        CharSequence desc = node.getContentDescription();
        if (desc != null && desc.length() > 0) {
            return desc.toString();
        }

        return null;
    }

    private String findTextInSiblings(AccessibilityNodeInfo parent) {
        if (parent == null) return null;

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child != null) {
                String text = findTextInNode(child);
                child.recycle();
                if (text != null) {
                    return text;
                }
            }
        }

        return null;
    }

    private boolean isInSplitScreen() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            return false;
        }

        List<AccessibilityWindowInfo> windows = getWindows();
        if (windows == null) return false;

        int appWindows = 0;
        for (AccessibilityWindowInfo window : windows) {
            if (window != null
                    && window.getType() == AccessibilityWindowInfo.TYPE_APPLICATION
                    && window.getRoot() != null) {
                appWindows++;
            }
        }

        return appWindows > 1;
    }
    public String getDefaultLauncherPackage(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            return resolveInfo.activityInfo.packageName;  // Returns the default launcher name as a String like "com.android.launcher3"
        }
        return "";  // No default launcher set (chooser dialog active)
    }
    public void lockTask(String lockedapppackagename) {
        Intent intent = new Intent(this, LockApp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lockedapppackagename",lockedapppackagename);
        startActivity(intent);
    }
    public void exitSplitScreen() {
        performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
    }
    @Override
    protected void onServiceConnected() {
        instance = this;
        try {
            File isaccessibilitypermissiongranted = new File(getDataDir() + "/accpermgranted");
            if (!isaccessibilitypermissiongranted.exists()) {
                isaccessibilitypermissiongranted.createNewFile();
            }
        } catch (Exception e) {
        }
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(getDataDir() + "/dataa");
            ObjectInputStream in = new ObjectInputStream(file);
            applocksettingss = (AppLockSettings) in.readObject();
            in.close();
            file.close();
            System.out.println("Object has been deserialized\nData after Deserialization.");
            //printData(object);

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        //info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        //info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        //info.notificationTimeout = 100;
        //setServiceInfo(info);
        //AccessibilityServiceInfo info = getServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED
                | AccessibilityEvent.TYPE_VIEW_FOCUSED
                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(info);
        Intent intent = new Intent(this, LogCatMonitorService.class);
        startService(intent);
    }
   @Override
   public void onAccessibilityEvent(AccessibilityEvent event) {
       String inputmethodpackagename = Settings.Secure.getString(getContentResolver(),"default_input_method");
       int indexoftheslashinthattext = inputmethodpackagename.indexOf("/");
       if (indexoftheslashinthattext != -1) {
           inputmethodpackagename = inputmethodpackagename.substring(0,indexoftheslashinthattext);
       }
       if (!event.getPackageName().toString().equals("android") && !event.getPackageName().toString().equals("com.android.systemui") && !event.getPackageName().toString().equals(inputmethodpackagename) && !event.getPackageName().toString().equals(getPackageName()) && !tempunlockedpackagenames.contains(event.getPackageName().toString())) {
           SettingsActivity.logoutFromSettings();
           tempunlockedpackagenames.clear();
       }
       if (!AuthenticationActivity.alreadyrunning && applocksettingss.isAppPackageNameLocked(event.getPackageName().toString())) {
           lockTask(event.getPackageName().toString());
       }
       if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
           ComponentName n = new ComponentName(event.getPackageName().toString(),event.getClassName().toString());
           String namee = n.getClassName();
           if (isNewAdbHostAdditionBlocked() && event.getPackageName() != null && event.getPackageName().toString().equals("com.android.systemui") && event.getText() != null && event.getText().size() > 1) {
           if (event.getText().get(0).toString().contains("USB") && event.getText().get(1).toString().contains(":")) {
               WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
               View overlay = new View(this);
               overlay.setBackgroundColor(Color.TRANSPARENT);
               WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
               params.gravity = Gravity.TOP;
               overlay.setOnTouchListener((v,eventt) -> true);
               wm.addView(overlay,params);
               performGlobalAction(GLOBAL_ACTION_BACK);
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
               }
               wm.removeView(overlay);
startActivity(new Intent(getApplicationContext(), UsbDebuggingAuthenticationActivity.class));
           }
           }
           if (!isNewAdbHostAdditionBlocked() && event.getPackageName().toString().equals("com.android.systemui") && event.getText() != null && event.getText().size() > 1) {
               if (event.getText().get(0).toString().contains("USB") && event.getText().get(1).toString().contains(":")) {
                 setAdbHostAdditionBlocked(true);
               }
           }
       }
       if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
           CharSequence eventText = null;

           if (event.getText() != null && !event.getText().isEmpty()) {
               eventText = event.getText().get(0);
           } else if (event.getContentDescription() != null) {
               eventText = event.getContentDescription();
           }

           AccessibilityNodeInfo source = event.getSource();
           if (eventText == null && source != null) {
               if (source.getText() != null) {
                   eventText = source.getText();
               } else if (source.getContentDescription() != null) {
                   eventText = source.getContentDescription();
               }
           }
           if (eventText == null) {
               eventText = "null";
           }
           if (eventText.equals("App info")) {
               eventText = "Settings";
           }
           if (!AuthenticationActivity.alreadyrunning && eventText != null && event.getPackageName() != null && event.getPackageName().toString().equals(getDefaultLauncherPackage(getApplicationContext())) && applocksettingss.isAppNameLocked(eventText.toString())) {
               lockTask(findPackageNameByFriendlyAppName(eventText.toString()));
           }
           if (eventText != null && eventText.toString().equals("Overview")) {
               DevicePolicyManager dpm;
               ComponentName admin;
               dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
               admin = new ComponentName(this, DeviceAdminReceiverForDeviceOwner.class);
           }
           if (source != null) {
               source.recycle();
           }
       }
   }
    @Override
    public void onInterrupt() {
    }
}