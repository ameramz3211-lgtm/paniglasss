package com.paaniglass.app;

import android.content.Context;
import android.content.pm.PackageManager;

final class BuildConfigHelper {
    private BuildConfigHelper() { }

    static String versionName(Context c) {
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }
}
