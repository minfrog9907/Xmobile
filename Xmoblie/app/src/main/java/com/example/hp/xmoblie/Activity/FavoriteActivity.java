package com.example.hp.xmoblie.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.hp.xmoblie.Adapter.StarItemAdapter;
import com.example.hp.xmoblie.Items.ShortCutItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-30.
 */

public class FavoriteActivity extends AppCompatActivity {
    ApiClient apiClient;
    ListView lvFavoriteList;
    List<ShortCutItem> shortCutItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        lvFavoriteList = (ListView) findViewById(R.id.lvFavoriteList);

        apiClient = ApiClient.service;
        getShortCutItem(0,10);

    }


    private void getShortCutItem(int offset, int limit) {//인자값 시작 , 끝 (총개수)
        final Call<List<ShortCutItem>> call = apiClient.repoStar(getIntent().getStringExtra("token"), offset, limit);//getIntent().getStringExtra("token"), base64String
        call.enqueue(new Callback<List<ShortCutItem>>() {
            @Override
            public void onResponse(Call<List<ShortCutItem>> call, Response<List<ShortCutItem>> response) {
                for(int i=0; i<response.body().size(); ++i){
                   shortCutItems.add(response.body().get(i));//데이터를 불러와 ShortCutItem.class형식으로 반환 StarItem확인 ㄲㄲ
                }
                adapterSetting();
            }

            @Override
            public void onFailure(Call<List<ShortCutItem>> call, Throwable t) {

            }
        });
    }

    public void adapterSetting() {
        StarItemAdapter adapter = new StarItemAdapter(this, shortCutItems);
        lvFavoriteList.setAdapter(adapter);
    }

    public void fileProtocol(String path) {
        shortCutItems = new ArrayList<>();

    }

}
