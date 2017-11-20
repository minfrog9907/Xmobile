package com.example.hp.xmoblie.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hp.xmoblie.Adapter.FileManagerListAdapter;
import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.HistoryItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Utill.HistorySharedPreferenceManager;

import java.util.ArrayList;


/**
 * Created by HP on 2017-11-10.
 */

public class HistoryActivity extends AppCompatActivity{

    ListView listView;
    ArrayList<HistoryItem> historyItems;
    ArrayList<FileItem> fileItems = new ArrayList<FileItem>();
    FileManagerListAdapter fileManagerListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_history, null);

        actionBar.setCustomView(mCustomView);

        historyItems=HistorySharedPreferenceManager.getInstance().getHistory();
        for(int i =0; i<historyItems.size(); ++i){
            fileItems.add(historyItems.get(i).getFileItem());
        }

        fileManagerListAdapter=new FileManagerListAdapter(this,fileItems);

        listView = (ListView)findViewById(R.id.history_fileList);
        listView.setAdapter(fileManagerListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(HistoryActivity.this,FileManagerActivity.class).putExtra("path",historyItems.get(position).getPath()));
            }
        });
    }
}
