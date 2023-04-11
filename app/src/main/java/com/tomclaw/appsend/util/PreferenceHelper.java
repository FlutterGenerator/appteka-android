package com.tomclaw.appsend.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tomclaw.appsend.R;

/**
 * Created with IntelliJ IDEA.
 * User: solkin
 * Date: 11/15/13
 * Time: 1:56 PM
 */
public class PreferenceHelper {

    public static boolean isDarkTheme(Context context) {
        return getBooleanPreference(context, R.string.pref_dark_theme, R.bool.pref_dark_theme_default);
    }

    public static boolean isShowSystemApps(Context context) {
        return getBooleanPreference(context, R.string.pref_show_system, R.bool.pref_show_system_default);
    }

    public static String getSortOrder(Context context) {
        return getStringPreference(context, R.string.pref_sort_order, R.string.pref_sort_order_default);
    }

    public static int getLastRunBuildNumber(Context context) {
        return getIntegerPreference(context, R.string.pref_last_run_build_number, R.integer.pref_last_run_build_number_default);
    }

    public static void updateLastRunBuildNumber(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            setIntegerPreference(context, R.string.pref_last_run_build_number, info.versionCode);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    public static boolean isShowUploadNotice(Context context) {
        return getBooleanPreference(context, R.string.pref_upload_notice, R.bool.pref_upload_notice_default);
    }

    public static void setShowUploadNotice(Context context, boolean value) {
        setBooleanPreference(context, R.string.pref_upload_notice, value);
    }

    private static boolean getBooleanPreference(Context context, int preferenceKey, int defaultValueKey) {
        return getSharedPreferences(context).getBoolean(context.getResources().getString(preferenceKey),
                context.getResources().getBoolean(defaultValueKey));
    }

    private static void setBooleanPreference(Context context, int preferenceKey, boolean value) {
        getSharedPreferences(context).edit().putBoolean(context.getResources().getString(preferenceKey),
                value).apply();
    }

    private static int getIntegerPreference(Context context, int preferenceKey, int defaultValueKey) {
        return getSharedPreferences(context).getInt(context.getResources().getString(preferenceKey),
                context.getResources().getInteger(defaultValueKey));
    }

    private static void setIntegerPreference(Context context, int preferenceKey, int value) {
        getSharedPreferences(context).edit().putInt(context.getResources().getString(preferenceKey),
                value).apply();
    }

    private static String getStringPreference(Context context, int preferenceKey, int defaultValueKey) {
        return getSharedPreferences(context).getString(context.getResources().getString(preferenceKey),
                context.getResources().getString(defaultValueKey));
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context),
                getSharedPreferencesMode());
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }
}
