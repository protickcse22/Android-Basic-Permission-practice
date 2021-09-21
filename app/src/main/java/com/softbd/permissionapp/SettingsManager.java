package com.softbd.permissionapp;

import android.content.Context;

public class SettingsManager {
    private static SettingsManager mSettingsManager;
    public Context mContext;
    private String TAG = getClass().getSimpleName();

    private SettingsManager() {

    }

    public static SettingsManager getInstance() {
        if (mSettingsManager == null)
            mSettingsManager = new SettingsManager();
        return mSettingsManager;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void firstTimeAskingAudioRecordPermission(boolean isFirstTime) {
        SharedPrefUtil.setSetting(mContext, Constants.VOICE_INPUT_PERMISSION, isFirstTime);
    }

    public boolean isFirstTimeAskingAudioRecordPermission() {
        return SharedPrefUtil.getBooleanSetting(mContext, Constants.VOICE_INPUT_PERMISSION, true);
    }

    public void firstTimeAskingForCameraPermission(boolean isFirstTime) {
        SharedPrefUtil.setSetting(mContext, Constants.CAMERA_PERMISSION, isFirstTime);
    }

    public boolean isTimeAskingForCameraPermission() {
        return SharedPrefUtil.getBooleanSetting(mContext, Constants.CAMERA_PERMISSION, true);
    }
}
