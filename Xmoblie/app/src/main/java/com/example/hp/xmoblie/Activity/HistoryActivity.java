package com.example.hp.xmoblie.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hp.xmoblie.Adapter.FileManagerListAdapter;
import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-10.
 */

public class HistoryActivity extends AppCompatActivity{
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //listView.setAdapter(new FileManagerListAdapter(this,);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileItemHolder fileItemHolder = (FileItemHolder) view.getTag();
                FileItem fileItem = fileItemHolder.realFileItem;
            }
        });
    }
}
