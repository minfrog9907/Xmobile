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
                moveIntent();
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
                        moveIntent();
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

    private void moveIntent(){
        finish();
        SharedPreferences autoLogin = getSharedPreferences("autoLogin",0);
        if(autoLogin.getBoolean("autoLogin",false)){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));// 액티비티 종료
        }else{
            TelephonyManager mgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            ApiClient apiClient = ApiClient.service;

            String deviceid = "testDevice";//mgr.getDeviceId();
            String userid="",password="";
            autoLogin.getString("id",userid);
            autoLogin.getString("password",password);
            Call<LoginItem> call = apiClient.repoContributors(userid, password, deviceid);
            call.enqueue(new Callback<LoginItem>() {
                @Override
                public void onResponse(Call<LoginItem> call,
                                       Response<LoginItem> response) {
                    switch (response.body().getStatus()) {
                        case 0:
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("username", response.body().getUsername());
                            intent.putExtra("token", response.body().getToken());
                            intent.putExtra("privilege", response.body().getPrivilege());
                            startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), "잘못된 아이디 또는 비밀번호입니다.", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), "잠긴 계정입니다.", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "알 수 없는 오류  " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<LoginItem> call, Throwable t) {
                    Log.e("jsonResponse", "빼애애앵ㄱ");

                }


            });
        }
        //startActivity(new Intent(getApplicationContext(), CheerPopUp.class));// 액티비티 종료

    }
}