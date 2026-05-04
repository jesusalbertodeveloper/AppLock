# Advanced Android App Lock - Device Owner API (Bypass-Proof)

**Lock apps securely on Android with this production-ready app lock using the privileged Device Owner API.**<br />Unlike traditional app lockers, it **prevents locked apps from launching entirely**, suspends/hides them from all launchers and Settings, disables Safe Mode bypass, and restricts critical features like app installs/uninstalls—even making itself un-uninstallable without authentication.
[![Linux](https://img.shields.io/badge/Android-any_architecture-brightgreen?logo=Android&logoColor=white)](https://github.com/jesusalbertodeveloper/tuifileexplorerapp)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen)](https://github.com/YOUR_USERNAME/REPO_NAME/releases)
[![GitHub stars](https://img.shields.io/github/stars/jesusalbertodeveloper/AppLock?style=social)](https://github.com/jesusalbertodeveloper/AppLock)

## Key Features
- **True App Blocking**: Prevents locked apps from launching (not just blocking interaction with them using an overlay on top of the app,which is what normal app lockers do).
- **Suspend & Hide Apps**: Prevent suspended apps from launching and hide apps from ALL launchers and Android's Settings
- **Disable Safe Mode**: Blocks Safe Mode to prevent bypasses.
- **Restrict Device Features**: Limits app installs/uninstalls, wallpaper changes, and more.
- **Ununinstall Protection**: Can't be removed without authentication (even in Safe Mode).
- **Recovery via ADB**: Test-only mode allows safe removal with `dpm remove-active-admin`.

**Perfect for**: Root-free, advanced app locking on stock Android. Works on most devices **except Knox-tripped Samsungs** (see Limitations).

## Installation (ADB Required)
1. Enable **USB Debugging** in Developer Options.
2. Install the test-only APK:  
   ```bash
   adb install -t appname.apk
   ```
3. Follow **on-screen instructions** to set as Device Owner.
4. **Set trusted ADB host**: Check "Always allow" and disable ADB timeout (Android 11+).

**Samsung Warning**: Knox-tripped Samsung Android devices devices (e.g., rooted then relocked One UI) **cannot provision Device Owner** on stock firmware—Samsung limitation, not fixable(however i will later make an app that uses root privileges for this exact same locking which can be used on Knox-tripped rooted Samsung Android devices as well).

## Important Android Messages (Not Bugs)
- **"Screen pinned/unpinned"**: Normal from Android's app pinning—required for launch blocking.
- **"Managed by your organization"**: Due to Device Owner API (corporate feature). You retain full control of the device; revoke anytime.

## Summary of the Privacy Policy
- **Zero Data Collection**: No personal, usage, or device data stored/transmitted.
- **No Remote Access**: Local-only; no internet permission or cloud sync.
- **Full Transparency**: All features (locks, suspends) work offline on-device.

## Summary of the Disclaimer & Risks(low risks)
**Use at your own risk**—Device Owner API is powerful and can brick devices if misconfigured (low risk, but backup data first).

- Provided "as is" with **no warranty**.
- **Cannot block**: Factory resets, recovery/bootloader reflashes (Android limits).
- **Safe for user apps**; do **NOT** hide/suspend system apps (risks bricking).
- Developer not liable for data loss, damage, or bypasses.

**Accept terms in-app** or don't install.

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

## Privacy Policy(the same that is shown in the app)
Privacy Policy:
1.No data collection:This app does not collect any personal information, device data, usage data, or any other data from your device.It does not store any sensitive information outside of your device’s local storage, and it never uploads or transmits such data to any server or third party.It does not share your data with advertisers, analytics providers, or any other third‑party services.
2.No remote access nor management: This app does not allow remote device access, remote device control, or remote management of your device.It cannot be used to track, monitor, or control your device from outside your physical control.NOTE:After setting this app as the device owner app Android will say something along the lines of "This device is managed by your organization" but this app doesn't remotely manage your device and it doesnt access nor change anything without your permission.The reason for this Android notice is that the Device Owner API is intended for managing devices owned by an organization but there's no issue with using this management for this non-enterprise purpose and it needs this management in order to lock apps in a better way than traditional app lock apps.The management is also used to suspend or hide apps and restrict certain device features such as Safe Mode.This management can be removed from the app's settings and removing it is necessary to be able to uninstall this app(it can't be uninstalled by normal means)
3. No internet connectivity This app does not use internet connectivity at all and doesnt use network connectivity This app does not request the Android "INTERNET" permission and therefore cannot connect to the internet.This is a local-only app that doesn't rely on any remote service.It doesn't use any cloud.
Because the app has no internet access, it is technically incapable of sending data to any server or cloud service.
All functionality—including the app lock, Device Owner features, and device‑management settings—operates locally on your device.
No data is ever transmitted or synchronized with external systems.

## Disclaimer(the same that is shown in the app)
Disclaimer:
This app uses the privileged Android Device Owner API, which can permanently damage or “brick” the device if something goes wrong.
This app is NOT intended to brick the device or cause permanent data loss. However, mistakes, bugs, unexpected Android behavior, or user‑configuration issues can still lead to a device state that cannot be fixed without a factory reset, or can cause the app to fail to operate correctly, including failing to lock apps or devices as expected. You assume all risk of device damage, including bricking, data loss, or repair costs, and all risk that the lock may not work as intended or may be bypassed.
Please note that this risk is very low and its quite unlikely you might have an issue with this app.
Also note that since the privileged Device Owner API is used the lock can be made way harder to bypass than in normal app lock apps you can find on places like the Google Play Store since the device owner app cannot be unset without proper authentication and safe mode can be disabled in this app so that the lock can't be bypassed with it.
Also if an app is suspended there is NO way to force start it unless you unsuspend it here and hiding an app here hides it in Android's settings as well so this hiding can be similar to not having the app installed at all.
Please note that hide/suspend also prevents the app from auto starting and from running in the background.Hide/suspend is NOT available for system apps as that could brick the device.Do NOT try to hide/suspend system apps as that could brick the device.You can safely lock system apps
The app is provided “as is” and without any warranty, express or implied, including warranties of performance, reliability, or security. I (the developer) will not be responsible for any data loss, device damage, unauthorized access, or any other issues arising from your use of this app, including the failure of the app to operate or the lock to work in any situation.
Please back up important data before continuing setup. If you do not accept these terms, tap “I Do Not Agree”; no changes will be made to your device.
--Recovery via ADB (recommended)--
This app’s Device Owner is intentionally declared as "test‑only" so that you can remove it using the command:   "dpm remove-active-admin com.jarm.applock"   from an ADB shell, which releases the Device Owner and lets you fix issues without a factory reset and without data loss.
This recovery method can only be used by trusted ADB hosts. Before proceeding:\n - Leave "USB debugging" enabled.
- Set a "trusted ADB host" by connecting this device to your computer with ADB and checking the option to “always allow from this computer” when prompted.
- If you are on Android 11 or later, please turn on the option to "disable ADB authorization timeout", so your trusted ADB host is not automatically untrusted.\n  The connection of new ADB hosts will be locked behind your app‑lock password, so someone else cannot simply add their own ADB host to remove the app lock.
--Important limitations-- 
- This app cannot block factory resets or firmware reflashes from recovery, download, or bootloader mode.
- It also cannot persist itself, its lock, or its settings after a factory reset or firmware reflash.
These are limitations of Android, not of this app.
The “Disable Factory Reset” option in the app only disables factory reset in the reset options of the Android Settings app. The device can still be factory reset from recovery/download/bootloader mode.
Recovery Options via ADB:
Remove device owner,app lock and settings:
settings put secure applock_emergency_remove 1
Disable safe mode:
settings put secure applock_safe_mode 2
Enable safe mode:
settings put secure applock_safe_mode 1
Reset app lock password:
settings put secure applock_password_reset 1
## License
[MIT License](LICENSE)
## Contribution
**Star/Fork if useful!** Questions? Open an issue.
