package com.trinidad.beansanconstruction.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class DeviceUtils {
    /**
     * <Get the version name for this app >
     *
     * @return
     */
    public static String getVersionName(Context c) {
        String packageName = c.getPackageName();
        try {
            return c.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
