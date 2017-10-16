package com.example.hp.xmoblie.Activity;

/**
 * Created by HP on 2017-09-20.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        int readStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);


        if (internetPermissionCheck == PackageManager.PERMISSION_DENIED
                ||storagePermissionCheck == PackageManager.PERMISSION_DENIED
                ||cameraPermissionCheck == PackageManager.PERMISSION_DENIED
                ||readStatePermissionCheck ==PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onBackPressed() { //super.onBackPressed(); }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


}