package com.softbd.permissionapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionManager {
    public enum PermissionType {
        CAMERA,
        AUDIO_RECORDER
    }

    static public PermissionManager mPermissionManager;
    protected Context mContext;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    public static final int AUDIO_RECORDER_PERMISSION_REQUEST_CODE = 102;
    public static final String[] AUDIO_RECORDER_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        if (mPermissionManager == null)
            mPermissionManager = new PermissionManager();
        return mPermissionManager;
    }

    public boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private boolean shouldAskAllPermission(Context context, String... permissions) {
        if (shouldAskPermission()) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return true;
            }
        }
        return false;
    }


    public void checkAllPermission(Context context, PermissionAskListener listener, PermissionType type, String... permissions) {
        boolean flag = false;
        if (shouldAskAllPermission(context, permissions)) {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, permission)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                listener.onPermissionPreviouslyDenied();
            } else {
                if (type == PermissionType.AUDIO_RECORDER) {
                    if (SettingsManager.getInstance().isFirstTimeAskingAudioRecordPermission()) {
                        SettingsManager.getInstance().firstTimeAskingAudioRecordPermission(false);
                        listener.onNeedPermission();
                    }
                    else {
                        listener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                    }
                } else if (type == PermissionType.CAMERA) {
                    if (SettingsManager.getInstance().isTimeAskingForCameraPermission()) {
                        SettingsManager.getInstance().firstTimeAskingForCameraPermission(false);
                        listener.onNeedPermission();
                    }
                    else {
                        listener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                    }
                }
            }
        } else {
            listener.onPermissionGranted();
        }

    }


    public interface PermissionAskListener {

        void onNeedPermission();

        void onPermissionPreviouslyDenied();

        void onPermissionPreviouslyDeniedWithNeverAskAgain();

        void onPermissionGranted();
    }

    public void showRationalDialog(final Context context, String title, String msg, final String... permissions) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context, permissions, PermissionManager.AUDIO_RECORDER_PERMISSION_REQUEST_CODE);
                        dialog.dismiss();
                    }
                }).show();

    }

    public void showSettingsDialog(final Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSettings(context);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void goToSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + context.getPackageName());
        intent.setData(uri);
        context.startActivity(intent);
    }
}
