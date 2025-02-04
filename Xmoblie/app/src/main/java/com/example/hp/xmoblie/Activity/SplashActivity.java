package com.example.hp.xmoblie.Activity;

/**
 * Created by HP on 2017-09-20.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Utill.BaseActivity;

/**
 * Created by triti on 2017-07-13.
 */

public class SplashActivity extends BaseActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    Intent intent;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        ServiceControlCenter.getInstance().setContext(this);


        intent = new Intent(SplashActivity.this, LoginActivity.class);

        Uri data = getIntent().getData();
        if (data != null && data.getQueryParameter("path") != null) {
            intent.putExtra("path", data.getQueryParameter("path"));
            intent.putExtra("pb", true);
        } else {
            intent.putExtra("pb", false);
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int internetPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            int storagePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int readStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int pravitePemissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS);

            //   Toast.makeText(getApplicationContext(),internetPermissionCheck+" "+storagePermissionCheck+" "+cameraPermissionCheck+" "+readStatePermissionCheck+" "+pravitePemissionCheck,Toast.LENGTH_SHORT).show();
            if (internetPermissionCheck == PackageManager.PERMISSION_DENIED
                    || storagePermissionCheck == PackageManager.PERMISSION_DENIED
                    || cameraPermissionCheck == PackageManager.PERMISSION_DENIED
                    || readStatePermissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PACKAGE_USAGE_STATS},
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
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);

            }
            firstSetting();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    }

    private void firstSetting() {
        SharedPreferences saveText = getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveText.edit();
        if (saveText.getInt("startHour", -1) == -1 &&
                saveText.getInt("endHour", -1) == -1 &&
                saveText.getInt("startMinute", -1) == -1 &&
                saveText.getInt("endMinute", -1) == -1 &&
                saveText.getInt("dProgress", -1) == -1) {
            editor.putInt("startHour", 8);
            editor.putInt("endHour", 17);
            editor.putInt("startMinute", 0);
            editor.putInt("endMinute", 0);
            editor.putInt("dProgress", 0);
        }
        editor.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
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