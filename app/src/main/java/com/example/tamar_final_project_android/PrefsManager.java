package com.example.tamar_final_project_android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * ניהול העדפות ונתונים קלים באמצעות SharedPreferences.
 */
public class PrefsManager {

    private static final String PREFS_NAME = "TripMatePrefs";
    private static final String KEY_LAST_USER = "last_username";
    private static final String KEY_FILTER_HOBBIES = "filter_by_hobbies";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastUsername(String username) {
        prefs.edit().putString(KEY_LAST_USER, username).apply();
    }

    public String getLastUsername() {
        return prefs.getString(KEY_LAST_USER, "");
    }

    public void setFilterByHobbies(boolean enabled) {
        prefs.edit().putBoolean(KEY_FILTER_HOBBIES, enabled).apply();
    }

    public boolean isFilterByHobbies() {
        return prefs.getBoolean(KEY_FILTER_HOBBIES, false);
    }
}