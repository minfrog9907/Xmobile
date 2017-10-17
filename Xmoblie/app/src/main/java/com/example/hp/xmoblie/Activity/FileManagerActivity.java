package com.example.hp.xmoblie.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileManagerActivity extends AppCompatActivity {
    ApiClient apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        apiClient = ApiClient.service;


    }
    private void fileProtocal(String path){
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"),path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                for(int i =0; i<response.body().size(); ++i){
                    //fileitem.java 파일 확인해서 사용 ㄱ
                }
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");

            }


        });
    }

}
