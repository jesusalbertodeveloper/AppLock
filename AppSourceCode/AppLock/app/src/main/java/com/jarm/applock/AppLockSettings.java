package com.jarm.applock;

import java.io.Serializable;
import java.util.ArrayList;

public class AppLockSettings implements Serializable {
    private ArrayList<String> lockedapppackagenames = new ArrayList<String>();
    private ArrayList<String> suspendedapppackagenames = new ArrayList<String>();
    private ArrayList<String> hiddenapppackagenames = new ArrayList<String>();

    private ArrayList<String> lockedappnames = new ArrayList<String>();
    private ArrayList<String> suspendedappnames = new ArrayList<String>();
    private ArrayList<String> hiddenappnames = new ArrayList<String>();
    private String password;
    public AppLockSettings() {
    }
    public boolean isAppPackageNameLocked(String logentrywithpackagename) {
        for (String packagename :lockedapppackagenames) {
            if (logentrywithpackagename.contains(packagename)) {
                if (AccSrvForBootstrapAndLock.tempunlockedpackagenames.contains(packagename)) {
                return false;
                }
                else {
                    return true;
                }
                }
        }
        return false;
    }
    public String findPackageNameFromLogEntry(String logentrywithpackagename) {
        for (String packagename :lockedapppackagenames) {
            if (logentrywithpackagename.contains(packagename)) {
                    return packagename;
            }
        }
        return "nulll";
    }
    public void addLockedAppPackageName(String packagename) {
        lockedapppackagenames.add(packagename);
    }
    public void addLockedAppName(String appname) {
        lockedappnames.add(appname);
    }
    public void removeLockedAppPackageName(String packagename) {
        lockedapppackagenames.remove(lockedapppackagenames.indexOf(packagename));
    }
    public void removeLockedAppName(String appname) {
        lockedappnames.remove(lockedappnames.indexOf(appname));
    }
    public boolean isAppPackageNameSuspended(String packagename) {
        for (String packagenamee :suspendedapppackagenames) {
            if (packagenamee.contains(packagename)) {
                return true;
            }
        }
        return false;
    }
    public boolean isAppPackageNameHidden(String packagename) {
        for (String packagenamee :this.lockedapppackagenames) {
            if (packagenamee.contains(packagename)) {
                return true;
            }
        }
        return false;
    }
    public boolean isAppNameLocked(String stringtocheck) {
        for (String appname :this.lockedappnames) {
            if (stringtocheck.contains(appname)) {
                return true;
            }
        }
        return false;
    }
}
