package com.jarm.applock;

public class AppListItemForAppLockSettings {
    private String apppackagename;

    private String appname;

    private boolean locked;
    private boolean suspended;
    private boolean hidden;
    private boolean systemapp;


    public AppListItemForAppLockSettings(String packagename, String userfacingname, boolean c, boolean d, boolean e,boolean g) {
        this.apppackagename = packagename;
        this.appname = userfacingname;
        this.locked = c;
        this.suspended = d;
        this.hidden = e;
        this.systemapp = g;
    }

    public String getText() {
        return this.appname + "\n" + this.apppackagename;
    }
    public String getPackageName() {
        return this.apppackagename;
    }
    public String getAppName() {
        return this.appname;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isSuspended() {
        return this.suspended;
    }
    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isSystemApp() {
        return this.systemapp;
    }

    public void setLocked(boolean checked) {
        this.locked = checked;
    }
}
