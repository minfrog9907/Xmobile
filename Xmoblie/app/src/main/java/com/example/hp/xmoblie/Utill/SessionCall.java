package com.example.hp.xmoblie.Utill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.MainActivity;
import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.Service.ApiClient;
import com.example.hp.xmoblie.Service.ServiceControlCenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HP on 2017-11-28.
 */

public class SessionCall {
    public  SessionCall(){}
    ApiClient apiClient = ApiClient.severService;

    public void SessionCall() {
        byte[] euckrStringBuffer;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write((ServiceControlCenter.getInstance().getToken()+"\0").getBytes(Charset.forName("euc-kr")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        euckrStringBuffer = outputStream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), euckrStringBuffer);

        Call<ResponseBody> call = apiClient.repoDownload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Success", "SuccessSession");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Success", "FailedSession");
            }
        });


    }

    public void SessionRecall() {
        SharedPreferences autoLogin = ServiceControlCenter.getInstance().getContext().getSharedPreferences("autoLogin", MODE_PRIVATE);
        String id = autoLogin.getString("id", "");
        String pw = autoLogin.getString("password", "");
        loginSystem(id, pw);
    }


    private void loginSystem(final String userid, final String password) {
        TelephonyManager mgr = (TelephonyManager) ServiceControlCenter.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);

        ApiClient apiClient2 = ApiClient.service;
        String deviceid = "Mobile2";
//mgr.getDeviceId();
        final Call<LoginItem> call = apiClient2.repoContributors(userid, password, deviceid);
        call.enqueue(new Callback<LoginItem>() {
            @Override
            public void onResponse(Call<LoginItem> call,
                                   Response<LoginItem> response) {
                switch (response.body().getStatus()) {
                    case 0:
                        ServiceControlCenter.getInstance().setToken(response.body().getToken());
                        SessionCall();
                        break;
                    case 1:
                        Toast.makeText(ServiceControlCenter.getInstance().getContext().getApplicationContext(), "잘못된 아이디 또는 비밀번호입니다.", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(ServiceControlCenter.getInstance().getContext().getApplicationContext(), "잠긴 계정입니다.", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(ServiceControlCenter.getInstance().getContext().getApplicationContext(), "알 수 없는 오류  " + response.body().getStatus(), Toast.LENGTH_LONG).show();
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
