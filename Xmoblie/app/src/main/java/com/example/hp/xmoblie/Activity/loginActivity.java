package com.example.hp.xmoblie.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText idEditText, pwEditText;
    String id, pw;
    SharedPreferences autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button submitLogin = (Button) findViewById(R.id.submitLogin);
        idEditText = (EditText) findViewById(R.id.idEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);
        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = idEditText.getText().toString();
                pw = pwEditText.getText().toString();
                loginSystem(id, pw);
                //  loginProcess(id, pw);
            }
        });

        autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
        boolean auto = false;
        auto = autoLogin.getBoolean("autoLogin", false);
        Log.e("autoLogin", auto + "");
        if (auto) {
            id = autoLogin.getString("id", "");
            pw = autoLogin.getString("password", "");
            Toast.makeText(getApplicationContext(), "자동로그인되었습니다.", Toast.LENGTH_LONG).show();
            loginSystem(id, pw);
        }
    }

    private void loginProcess(String id, String pw) {
        SharedPreferences.Editor editor = autoLogin.edit();
        editor.putBoolean("autoLogin", true);
        editor.putString("id", id);
        editor.putString("password", pw);
        editor.commit();

    }

    private void loginSystem(final String userid, final String password) {
        TelephonyManager mgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        ApiClient apiClient = ApiClient.service;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String deviceid = "testDevice";
                //mgr.getDeviceId();
        final Call<LoginItem> call = apiClient.repoContributors(userid, password, deviceid);
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
                        loginProcess(userid,password);
                        startActivity(intent);
                        finish();
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


}
