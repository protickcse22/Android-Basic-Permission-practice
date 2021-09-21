package com.softbd.permissionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private Button permissionButton;
    private View.OnClickListener grantPermission = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            permissionForAudioRecorder(PermissionManager.AUDIO_RECORDER_PERMISSION);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionButton = findViewById(R.id.bt_grant_permission);
        permissionButton.setOnClickListener(grantPermission);
    }

    private void permissionForAudioRecorder(final String... permissions) {
        PermissionManager.getInstance().checkAllPermission(MainActivity.this, new PermissionManager.PermissionAskListener() {
            @Override
            public void onNeedPermission() {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, PermissionManager.AUDIO_RECORDER_PERMISSION_REQUEST_CODE);
            }

            @Override
            public void onPermissionPreviouslyDenied() {
                PermissionManager.getInstance().showRationalDialog(MainActivity.this,"Permission Denied",
                        "Without this permission this app is unable to give voice input. Are you sure you want to deny this permission.",PermissionManager.AUDIO_RECORDER_PERMISSION);
            }

            @Override
            public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                PermissionManager.getInstance().showSettingsDialog(MainActivity.this,"Permission Denied", "Now you must allow audio recorder and storage access from settings.");
            }

            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        }, PermissionManager.PermissionType.AUDIO_RECORDER, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.AUDIO_RECORDER_PERMISSION_REQUEST_CODE:
                boolean flag = false;
                if (grantResults.length > 0) {
                    for (int i : grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            flag = true;
                        } else {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        Toast.makeText(this, "Permission granted first", Toast.LENGTH_SHORT).show();
                    } else {
                        // Permission was denied.......
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case PermissionManager.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}