# Advanced Android App Lock - Device Owner API (Bypass-Proof)(Fully Open-Source)

**Lock apps securely on Android with this production-ready app lock using the privileged Device Owner API.**<br />Unlike traditional app lockers, it **prevents locked apps from launching entirely**, suspends/hides them from all launchers and Settings, disables Safe Mode bypass, and restricts critical features like app installs/uninstalls—even making itself un-uninstallable without authentication.
[![Linux](https://img.shields.io/badge/Android-any_architecture-brightgreen?logo=Android&logoColor=white)](https://github.com/jesusalbertodeveloper/tuifileexplorerapp)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen)](https://github.com/jesusalbertodeveloper/AppLock/releases)
[![GitHub stars](https://img.shields.io/github/stars/jesusalbertodeveloper/AppLock?style=social)](https://github.com/jesusalbertodeveloper/AppLock)

## Key Features
- **True App Blocking**: Prevents locked apps from launching (not just blocking interaction with them using an overlay on top of the app,which is what normal app lockers do).
- **Suspend & Hide Apps**: Prevent suspended apps from launching and hide apps from ALL launchers and Android's Settings
- **Disable Safe Mode**: Blocks Safe Mode to prevent bypasses.
- **Restrict Device Features**: Restricts certain Android OS features at a deep level such as installation/uninstallation of apps,addition/removal of accounts, wallpaper changes, and more.
- **Ununinstall Protection**: Can't be removed without authentication (even in Safe Mode).
- **Recovery via ADB**: Test-only mode allows safe removal with `dpm remove-active-admin`.

**Perfect for**: Root-free, advanced app locking on stock Android. Works on most devices **except Knox-tripped Samsungs** (see Limitations).

## Download Options
Download from either ReleaseApk(base.apk) in this repo or a Release(applock.apk)<br />
The source code is in AppSourceCode in this repo and you can compile the app from source<br />This app was made in Linux Mint 21.3 with Android Studio 2024.3.1 Patch 1

## Installation (ADB Required)
1. Enable **USB Debugging** in Developer Options.
2. Install the test-only APK:  
   ```bash
   adb install -t whatevernameyourapkhas.apk
   ```
3. Follow **on-screen instructions** to set as Device Owner.
4. **Set trusted ADB host**: Check "Always allow" and disable ADB timeout (Android 11+).

**Samsung Warning**: Knox-tripped Samsung Android devices devices (e.g., rooted then relocked One UI) **cannot provision Device Owner** on stock firmware—Samsung limitation, not fixable(however i will later make a version of this app that uses root privileges for this exact same locking which can be used on Knox-tripped rooted Samsung Android devices as well).

## Important Android Messages (Not Bugs)
- **"Screen pinned/unpinned"**: Normal from Android's app pinning—required for launch blocking.
- **"Managed by your organization"**: Due to Device Owner API (corporate feature). You retain full control of the device; revoke anytime.

## Summary of the Privacy Policy
- **Zero Data Collection**: No personal, usage, or device data stored/transmitted.
- **No Remote Access**: Local-only; no internet permission or cloud sync.
- **Full Transparency**: All features (locks, suspends) work offline on-device.<br />[Full Privacy Policy(same as in-app)](PRIVACY_POLICY.md)


## Summary of the Disclaimer & Risks(low risks)
**Use at your own risk**—Device Owner API is powerful and can brick devices if misconfigured (low risk, but backup data first).

- Provided "as is" with **no warranty**.
- **Cannot block**: Factory resets, recovery/bootloader reflashes (Android limits).
- **Safe for user apps**; do **NOT** hide/suspend system apps (risks bricking)(the app won't allow this but still don't try this).
- Developer not liable for data loss, damage, or bypasses.<br />[Full Disclaimer(same as in-app)](DISCLAIMER.md)

**Accept terms in-app(you can also read them here before downloading the app)** or don't install.


## Recovery Options (ADB Shell)
Keep USB Debugging on and trusted host set. New ADB connections require your app lock password.

```bash
# ADB commands for emergency recovery

# Remove Device Owner and this app's settings(recommended)
settings put secure applock_emergency_remove 1

# Remove Device Owner(only if former command didnt work,in which case set the previous setting to 0)
dpm remove-active-admin com.jarm.applock

# Disable Safe Mode
settings put secure applock_safe_mode 2

# Enable Safe Mode
settings put secure applock_safe_mode 1

# Reset password
settings put secure applock_password_reset 1
```

**Backup ADB keys** (~/.android folder) for recovery.
If you run the settings commands change the respective setting to 0 after finishing if the command didn't work(the app will change it to 0 automatically if it worked)
From an app with Android's APIs Android does NOT allow removing a secure setting so the app sets it to 0 after command execution but you can manually delete it if you wish

## Limitations
- No protection against factory reset/recovery mode.
- Hide/Suspend: Blocks auto-start/background; unavailable for system apps.
- Samsung Knox: Provisioning blocked on tripped devices.

## Why This App Lock is Better
Traditional Play Store lockers are easily bypassed via Safe Mode. This uses **Device Owner API** for military-grade protection—apps vanish completely until unlocked.


## License
[MIT License](LICENSE)
## Contribution
**Star/Fork if useful!** Questions? Open an issue.
