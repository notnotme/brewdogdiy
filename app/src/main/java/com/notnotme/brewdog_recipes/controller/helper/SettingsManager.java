package com.notnotme.brewdog_recipes.controller.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

public final class SettingsManager {

    private static final String KEY_LAST_SYNC = "KEY_LAST_SYNC";

    private SharedPreferences mSharedPreferences;

    public SettingsManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Date getLastSyncDate() {
        return new Date(mSharedPreferences.getLong(KEY_LAST_SYNC, 0));
    }

    public void setLastSyncDate(Date date) {
        mSharedPreferences.edit().putLong(KEY_LAST_SYNC, date.getTime()).apply();
    }

}
