package com.example.hp.xmobile;

/**
 * Created by HP on 2017-09-20.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by triti on 2017-07-13.
 */

public class SplashActivity extends BaseActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        int internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int storagePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);


        if (internetPermissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        if (storagePermissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        if (cameraPermissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        storagePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (internetPermissionCheck != PackageManager.PERMISSION_DENIED && storagePermissionCheck != PackageManager.PERMISSION_DENIED && cameraPermissionCheck != PackageManager.PERMISSION_DENIED) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(getApplicationContext(), CheerPopUp.class));// 액티비티 종료
                }
            }, SPLASH_DISPLAY_LENGTH);

        }


    }

    @Override
    public void onBackPressed() { //super.onBackPressed(); }
    }
}