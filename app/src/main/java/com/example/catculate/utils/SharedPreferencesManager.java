package com.example.catculate.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.example.catculate.App;
import timber.log.Timber;

public class SharedPreferencesManager {

  public SharedPreferencesManager() {
  }

  private static SharedPreferences getDefaultSharedPref() {
    return PreferenceManager.getDefaultSharedPreferences(App.getApp());
  }

  public void saveString(String key, String value) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    sharedPref.edit().putString(key, value).apply();
  }

  public void saveBoolean(String key, boolean value) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    sharedPref.edit().putBoolean(key, value).apply();
  }

  public void saveInt(String key, int value) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    sharedPref.edit().putInt(key, value).apply();
  }

  public String loadString(String key, String defaultValue) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    return sharedPref.getString(key, defaultValue);
  }

  public boolean loadBoolean(String key) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    return sharedPref.getBoolean(key, false);
  }

  public int loadInt(String key) {
    SharedPreferences sharedPref = getDefaultSharedPref();
    return sharedPref.getInt(key, 0);
  }

  public void clearData(String key) {
    Editor editor = getDefaultSharedPref().edit();
    editor.remove(key);
    editor.apply();
  }

  public void clearAll() {
    Timber.d("clearAll");
    Editor editor = getDefaultSharedPref().edit();
    editor.clear();
    editor.apply();
  }
}
