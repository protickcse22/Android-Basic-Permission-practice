package com.softbd.permissionapp;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context context) {
        SettingsManager.getInstance().setContext(context);
        super.attachBaseContext(context);
    }
}
