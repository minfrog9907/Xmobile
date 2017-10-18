package com.example.hp.xmoblie.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Adapter.BaseExpandableAdapter;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.example.hp.xmoblie.Animation.ResizeAnimation;


import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;


import org.w3c.dom.Text;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileManagerActivity extends ActionBarActivity {

    private Spinner spinnerOrder, spinnerSort;
    private LinearLayout spinnerList;
    private ImageView showSortWay;
    private AnimatedExpandableListAdapter listAdapter;
    private AnimatedExpandableListView expListView;
    private List<FileItem> listDataHeader;
    private HashMap<FileItem, List<FileItem>> listDataChild;
    private static int orderData = 0, sortData = 0;
    private static String searchData = "\\";

    private ApiClient apiClient;

    private final static Comparator<FileItem> comparator = new Comparator<FileItem>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(FileItem o, FileItem t1) {
            return collator.compare(o.getFilename(), t1.getFilename());
        }

    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        spinnerList = (LinearLayout) findViewById(R.id.spinnerList);
        showSortWay = (ImageView) findViewById(R.id.showSortWay);
        apiClient = ApiClient.service;

        showFileList(searchData);
        spinnerArray();

        //ActionBar 설정
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_filemanager, null);
        actionBar.setCustomView(mCustomView);


        //정렬기준 설정
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

        //ExpandableList onClickListener
        expListView.setOnGroupClickListener(new AnimatedExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                ImageView fileIcon = (ImageView) view.findViewById(R.id.fileIcon);

                if (fileIcon.getTag().equals("file")) {
                    Toast.makeText(FileManagerActivity.this, "Open File", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    ImageView imageView = (ImageView) view.findViewById(R.id.showMoreMenuimg);
                    if(imageView.getRotation() == 180){
                        imageView.setRotation(0);
                    }else {
                        imageView.setRotation(180);
                    }

                    if (expListView.isGroupExpanded(i)) {
                        expListView.collapseGroupWithAnimation(i);
                    } else {
                        expListView.expandGroupWithAnimation(i);
                    }

                    return true;
                }
            }
        });

        expListView.setOnChildClickListener(new AnimatedExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                ImageView fileIcon = (ImageView) view.findViewById(R.id.childFileIcon);
                if (fileIcon.getTag().equals("file")) {
                    Toast.makeText(FileManagerActivity.this, "Open File", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    TextView textView = (TextView) view.findViewById(R.id.childFileName);
                    searchData = searchData + "" + textView.getText();
                    showFileList(searchData);
                    return true;
                }
            }
        });

        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderData = i;
                showFileList(searchData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void spinnerArray() {
        String[] sortString = getResources().getStringArray(R.array.spinnerSort);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortString);
        spinnerSort.setAdapter(sortAdapter);

        String[] orderString = getResources().getStringArray(R.array.spinnerOrder);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderString);
        spinnerOrder.setAdapter(orderAdapter);
    }

    private void fileProtocal(String path) {

        listDataHeader = new ArrayList<>();
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"), path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                for (int i = 0; i < response.body().size(); ++i) {
                    listDataHeader.add(response.body().get(i));
                    if (response.body().get(i).getType() == 16) {
                        childFileProtocal(response.body().get(i));
                    }

                    //fileitem.java 파일 확인해서 사용 ㄱ
                }
                sortData();
                adaptList();
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");

            }
        });
    }

    private void childFileProtocal(final FileItem parantsData) {

        listDataChild = new HashMap<FileItem, List<FileItem>>();
        String path = searchData + "" + parantsData.getFilename();
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"), path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                List<FileItem> childList = new ArrayList<FileItem>();
                for (int i = 0; i < response.body().size(); ++i) {

                    childList.add(response.body().get(i));
                    listDataChild.put(parantsData, childList); // Header, Child data
                }
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");

            }

            //        for(int i = 0; i<5; i++){
//            listDataHeader.add(getFilesDir().getName());
//            List<String> childList = new ArrayList<String>();
//            for(int j = 0; j<i; j++){
//                childList.add(i + " - " + j);
//            }
//            listDataChild.put(listDataHeader.get(i), childList); // Header, Child data
//        }
        });
    }

    private void showFileList(String path) {
        //list 설정
        // get the listview
        expListView = (AnimatedExpandableListView) findViewById(R.id.fileList);

        // preparing list data
        fileProtocal(path);

    }

    private void adaptList() {
        listAdapter = new BaseExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void sortData() {
        switch (orderData) {
            case 0:
                Collections.sort(listDataHeader, comparator);
                System.out.println("sorting1");
                break;
            case 1:
                Collections.sort(listDataHeader, comparator);
                Collections.reverse(listDataHeader);
                System.out.println("sorting2");
                break;
            default:
                listDataHeader.sort(comparator);
                System.out.println("sortingd");
                break;
        }
    }

}
