package com.jarm.applock;

import static androidx.core.content.ContextCompat.getDataDir;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ListViewAdapterForSettings extends ArrayAdapter<AppListItemForAppLockSettings> {

    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
        CheckBox checkBox1;
        CheckBox checkBox2;

    }

    public ListViewAdapterForSettings(Context context, ArrayList<AppListItemForAppLockSettings> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.app_list_item_in_applock_settings, parent, false);

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textView);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.checkBox1 = convertView.findViewById(R.id.checkBox1);
            holder.checkBox2 = convertView.findViewById(R.id.checkBox2);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppListItemForAppLockSettings item = getItem(position);
        holder.textView.setText(item.getText());
        holder.checkBox.setChecked(item.isLocked());
        holder.checkBox1.setChecked(item.isSuspended());
        holder.checkBox2.setChecked(item.isHidden());


        // Optional: update model when checkbox is toggled
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setLocked(isChecked);
            AppLockSettings settingsmodify = new AppLockSettings();
            try {
                FileInputStream file = new FileInputStream(getDataDir(getContext()) + "/dataa");
                ObjectInputStream in = new ObjectInputStream(file);
                settingsmodify = (AppLockSettings) in.readObject();
                in.close();
                file.close();
            } catch (IOException ex) {
            } catch (ClassNotFoundException ex) {
            }
            if (SettingsActivity.isAuthenticationSuccessful()) {
                if (isChecked) {
                settingsmodify.addLockedAppPackageName(item.getPackageName());
                settingsmodify.addLockedAppName(item.getAppName());
            } else {
                settingsmodify.removeLockedAppPackageName(item.getPackageName());
                settingsmodify.removeLockedAppName(item.getAppName());
            }
            try {
                FileOutputStream file = new FileOutputStream(getDataDir(getContext()) + "/dataa");
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(settingsmodify);
                out.close();
                file.close();
            } catch (IOException ex) {
            }
            AccSrvForBootstrapAndLock.getInstance().applocksettingss = settingsmodify;
        }
        });
        holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (SettingsActivity.isAuthenticationSuccessful()) {
                if (isChecked) {
                    if (!item.isSystemApp()) {
                        DevicePolicyManager dpm = (DevicePolicyManager) this.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName admin = new ComponentName(this.getContext(), DeviceAdminReceiverForDeviceOwner.class);
                        dpm.setPackagesSuspended(admin, new String[]{item.getPackageName()}, true);
                    } else {
                        holder.checkBox1.setChecked(false);
                        Toast.makeText(getContext(), "Can't suspend a system app as that can damage your device.However you can still lock it", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    DevicePolicyManager dpm = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName admin = new ComponentName(this.getContext(), DeviceAdminReceiverForDeviceOwner.class);
                    dpm.setPackagesSuspended(admin, new String[]{item.getPackageName()}, false);
                }
            }
        });
        holder.checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
if (SettingsActivity.isAuthenticationSuccessful()) {
            if (isChecked) {
                if (!item.isSystemApp()) {
                    DevicePolicyManager dpm = (DevicePolicyManager) this.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName admin = new ComponentName(this.getContext(), DeviceAdminReceiverForDeviceOwner.class);
                    dpm.setApplicationHidden(admin, item.getPackageName(), true);
                } else {
                    holder.checkBox2.setChecked(false);
                    Toast.makeText(getContext(), "Can't hide a system app as that can damage your device.However you can still lock it", Toast.LENGTH_SHORT).show();
                }
            } else {
                DevicePolicyManager dpm = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName admin = new ComponentName(this.getContext(), DeviceAdminReceiverForDeviceOwner.class);
                dpm.setApplicationHidden(admin, item.getPackageName(), false);
            }
        }
        });
        return convertView;
    }
 }