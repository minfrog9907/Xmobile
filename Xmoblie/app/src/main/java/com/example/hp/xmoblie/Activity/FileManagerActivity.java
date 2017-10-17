package com.example.hp.xmoblie.Activity;

<<<<<<< HEAD

=======
import android.support.v7.app.ActionBarActivity;
>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.hp.xmoblie.Adapter.BaseExpandableAdapter;
import com.example.hp.xmoblie.Animation.ResizeAnimation;
<<<<<<< HEAD

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.LoginItem;
=======

import com.example.hp.xmoblie.Items.FileItem;
>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

<<<<<<< HEAD

=======
>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc
public class FileManagerActivity extends ActionBarActivity {

    private Spinner spinnerOrder, spinnerSort;
    private LinearLayout spinnerList;
    private ImageView showSortWay;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

<<<<<<< HEAD
    private ApiClient apiClient;
=======
    ApiClient apiClient;
>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);


        //ActionBar 설정
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_filemanager, null);
        actionBar.setCustomView(mCustomView);


        //정렬기준 설정
        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        spinnerList = (LinearLayout) findViewById(R.id.spinnerList);
        showSortWay = (ImageView) findViewById(R.id.showSortWay);

        fileProtocal("\\");

        showSortWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wrap = 60;
                ResizeAnimation resizeAnimation = new ResizeAnimation(spinnerList);
                resizeAnimation.setDuration(500);

                if (showSortWay.getRotation() == 180) {
                    resizeAnimation.setParams(wrap, 0);
                    spinnerList.startAnimation(resizeAnimation);
                    showSortWay.setRotation(0);
                } else {
                    resizeAnimation.setParams(0, wrap);
                    spinnerList.startAnimation(resizeAnimation);
                    showSortWay.setRotation(180);
                }
            }
        });

        //list 설정
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.fileList);

        // preparing list data
        prepareListData();

        listAdapter = new BaseExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        spinnerArray();
    }

    private void spinnerArray() {
        String[] sortString = getResources().getStringArray(R.array.spinnerSort);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortString);
        spinnerSort.setAdapter(sortAdapter);

        String[] orderString = getResources().getStringArray(R.array.spinnerOrder);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderString);
        spinnerOrder.setAdapter(orderAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


<<<<<<< HEAD
        fileProtocal("\\");

//        for(int i = 0; i<5; i++){
//            listDataHeader.add(getFilesDir().getName());
//            List<String> childList = new ArrayList<String>();
//            for(int j = 0; j<i; j++){
//                childList.add(i + " - " + j);
//            }
//            listDataChild.put(listDataHeader.get(i), childList); // Header, Child data
//        }

        apiClient = ApiClient.service;
=======
        for(int i = 0; i<5; i++){
            listDataHeader.add(String.valueOf(i));
            List<String> childList = new ArrayList<String>();
            for(int j = 0; j<i; j++){
                childList.add(i + " - " + j);
            }
            listDataChild.put(listDataHeader.get(0), childList); // Header, Child data
        }

        apiClient = ApiClient.service;

>>>>>>> 0b6ca1f5de8989e657bd8e5bf4ffa466d8121fcc

    }
    private void fileProtocal(String path){
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"),path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                for(int i =0; i<response.body().size(); ++i){
                    System.out.println(response.body().get(i));
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
