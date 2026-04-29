package com.jarm.applock;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceAdminReceiverForDeviceOwner extends android.app.admin.DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This app's device owner isn't correctly set so this app can't operate properly.If you want to uninstall this app press the OK/Accept button on your device otherwise check whether there are no accounts/work profiles/secondary user accounts and try setting this app's device owner again(You can add accounts/work profiles/secondary user accounts after this app's device owner is properly set)";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
    }
}