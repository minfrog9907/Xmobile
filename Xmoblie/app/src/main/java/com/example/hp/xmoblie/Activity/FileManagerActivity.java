package com.example.hp.xmoblie.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Adapter.BaseExpandableAdapter;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.example.hp.xmoblie.Animation.ResizeAnimation;


import com.example.hp.xmoblie.Custom.CustomFilemanagerBtnGroup;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileManagerActivity extends ActionBarActivity {

    private Spinner spinnerOrder, spinnerSort;
    private LinearLayout spinnerList, searchBtn, noFIleTxt;
    private ImageView showSortWay;
    private AutoCompleteTextView searchEdit;
    private AnimatedExpandableListView expListView;
    private List<FileItem> listDataHeader;
    private HashMap<FileItem, List<FileItem>> listDataChild;
    private int orderData = 0, sortData = 0, orderCheck = 0, sortCheck = 0;
    private String searchData = "\\";
    private boolean selectMode = false;
    private CustomFilemanagerBtnGroup cfbg;
    private int checkedItem = 0;
    private BaseExpandableAdapter listAdapter;
    private ScrollView fileListScroll;

    private ApiClient apiClient;

    private List<String> searchHistory = new ArrayList<String>();
    private List<String> moveDirHistory = new ArrayList<String>();

    private final static Comparator<FileItem> comparator = new Comparator<FileItem>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(FileItem o, FileItem t1) {
            return collator.compare(o.getFilename(), t1.getFilename());
        }

    };

    private final static Comparator<String> comparatorH = new Comparator<String>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(String o, String t1) {
            return collator.compare(o, t1);
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        spinnerList = (LinearLayout) findViewById(R.id.spinnerList);
        searchBtn = (LinearLayout) findViewById(R.id.searchBtn);
        noFIleTxt = (LinearLayout) findViewById(R.id.noFIleTxt);
        showSortWay = (ImageView) findViewById(R.id.showSortWay);
        searchEdit = (AutoCompleteTextView) findViewById(R.id.searchEdit);
        expListView = (AnimatedExpandableListView) findViewById(R.id.fileList);
        apiClient = ApiClient.service;
        cfbg = (CustomFilemanagerBtnGroup) findViewById(R.id.cfbg);
        fileListScroll = (ScrollView) findViewById(R.id.fileListScroll);

        Toast.makeText(this, "뒤로가기 버튼을 꾹 누르면 파일 매니저가 종료됩니다.", Toast.LENGTH_SHORT).show();
        getSearchHistroy();
        moveDir(searchData);
        spinnerArray();

        //ActionBar 설정
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_filemanager, null);
        actionBar.setCustomView(mCustomView);

        //Bottom Navigation 설정


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

                if (!selectMode) {
                    ImageView fileIcon = (ImageView) view.findViewById(R.id.fileIcon);

                    if (fileIcon.getTag().equals("file")) {
                        Toast.makeText(FileManagerActivity.this, "Open File", Toast.LENGTH_SHORT).show();
                    } else {
                        ImageView imageView = (ImageView) view.findViewById(R.id.showMoreMenu);
                        if (imageView.getRotation() == 180) {
                            imageView.setRotation(0);
                        } else {
                            imageView.setRotation(180);
                        }

                        if (expListView.isGroupExpanded(i)) {
                            expListView.collapseGroupWithAnimation(i);
                        } else {
                            expListView.expandGroupWithAnimation(i);
                        }
                    }
                } else {

                }
                return true;
            }
        });

        //ExpandableList onChildClickListener
        expListView.setOnChildClickListener(new AnimatedExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (!selectMode) {
                    ImageView fileIcon = (ImageView) view.findViewById(R.id.childFileIcon);
                    if (fileIcon.getTag().equals("file")) {
                        Toast.makeText(FileManagerActivity.this, "Open File", Toast.LENGTH_SHORT).show();
                    } else {
                        FileItem parants = (FileItem) expandableListView.getItemAtPosition(i);
                        TextView textView = (TextView) view.findViewById(R.id.childFileName);
                        searchData = checkRoot() + "" + parants.getFilename() + "\\" + textView.getText();
                        moveDir(searchData);
                    }
                } else {


                }
                return true;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeSelectMode();
                return true;
            }
        });


        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (++orderCheck > 1) {
                    orderData = i;
                    moveDirwithoutHistory(searchData);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    searchBtn.performClick();
                    return true;
                }
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchHistory(searchEdit.getText().toString());
                searchEdit.clearFocus();
                moveDir(searchEdit.getText().toString());
            }
        });

        fileListScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.d("asdf", "asdf");
            }
        });

    }

    /* 경로 이동 및 파일 실행 */

    private void fileProtocal(String path) {
        listDataHeader = new ArrayList<>();
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"), path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                if (response.body() != null && response.errorBody() == null) {
                    for (int i = 0; i < response.body().size(); ++i) {
                        listDataHeader.add(response.body().get(i));
                        if (response.body().get(i).getType() == 16) {
                            childFileProtocal(response.body().get(i));
                        }

                        //fileitem.java 파일 확인해서 사용 ㄱ
                    }

                    if (response.body().size() <= 0) {
                        noFIleTxt.setVisibility(View.VISIBLE);
                    } else {
                        noFIleTxt.setVisibility(View.INVISIBLE);
                    }

                    sortData();
                    adaptList();
                } else {
                    fileProtocal(searchData);
                }
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");

            }
        });

    }

    private void childFileProtocal(final FileItem parantsData) {

        listDataChild = new HashMap<FileItem, List<FileItem>>();
        String path = checkRoot() + "" + parantsData.getFilename();

        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"), path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                List<FileItem> childList = new ArrayList<FileItem>();
                if (response.body().isEmpty()) {
                    listDataChild.put(parantsData, childList);
                } else {
                    for (int i = 0; i < response.body().size(); ++i) {
                        childList.add(response.body().get(i));
                        listDataChild.put(parantsData, sortChildData(childList)); // Header, Child data
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");

            }
        });
    }

    private void searchFile(String path) {
        searchData = path;
        if (searchData.isEmpty() || searchData.equals("")) {
            searchData = "\\";
        } else if (searchData.length() > 1 && (searchData.charAt(searchData.length() - 1) == '/' || searchData.charAt(searchData.length() - 1) == '\\')) {
            searchData = searchData.substring(0, path.length() - 1);
        }
        searchData = searchData.replace("/", "\\");
        searchEdit.setText(searchData.replace("\\", "/"));
        searchEdit.setSelection(searchEdit.length());

        fileProtocal(searchData);
    }

    private void moveDir(String path) {
        Log.d("moveDir", "moveDir");
        moveDirHistory.add(path);
        searchFile(path);
    }

    private void moveDirwithoutHistory(String path) {
        searchFile(path);
    }

    private void adaptList() {
        listAdapter = new BaseExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /* 검색 및 정렬 */

    private void spinnerArray() {
        String[] sortString = getResources().getStringArray(R.array.spinnerSort);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortString);
        spinnerSort.setAdapter(sortAdapter);

        String[] orderString = getResources().getStringArray(R.array.spinnerOrder);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderString);
        spinnerOrder.setAdapter(orderAdapter);
    }

    private void sortData() {
        switch (orderData) {
            case 0:
                Collections.sort(listDataHeader, comparator);
                break;
            case 1:
                Collections.sort(listDataHeader, comparator);
                Collections.reverse(listDataHeader);
                break;
            default:
                Collections.sort(listDataHeader, comparator);
                break;
        }
    }

    private List<FileItem> sortChildData(List<FileItem> childData) {
        switch (orderData) {
            case 0:
                Collections.sort(childData, comparator);
                break;
            case 1:
                Collections.sort(childData, comparator);
                Collections.reverse(childData);
                break;
            default:
                Collections.sort(childData, comparator);
                break;
        }
        return childData;
    }

    private String checkRoot() {
        return searchData.equals("\\") ? searchData : searchData + "\\";
    }

    /* 검색 기록 */

    private void setSearchHistory(String history) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        Collections.reverse(searchHistory);

        if (searchHistory.contains(history)) {
            searchHistory.remove(history);
        }
        searchHistory.add(history);

        if (searchHistory.size() > 6) {
            searchHistory.remove(0);
        }
        setSearchAdapter();

        for (int i = 0; i < searchHistory.size(); i++) {
            a.put(searchHistory.get(i));
        }
        if (!searchHistory.isEmpty()) {
            editor.putString("searchHistory", a.toString());
        } else {
            editor.putString("searchHistory", null);
        }
        editor.apply();
        Collections.reverse(searchHistory);

    }

    private void getSearchHistroy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = prefs.getString("searchHistory", null);
        List<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        searchHistory = urls;
        setSearchAdapter();
    }

    private void setSearchAdapter() {
        searchEdit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchHistory));
    }

    /* 다중 선택 */

    private void changeSelectMode() {
        AnimatedExpandableListView l = expListView;
        int count = l.getChildCount();
        for (int i = 0; i < count; ++i) {

            ViewGroup row = (ViewGroup) l.getChildAt(i);

            System.out.println(row);

//            CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
//            ImageView imageView = (ImageView) row.findViewById(R.id.showMoreMenu);
//            check.setVisibility(View.VISIBLE);
//            imageView.setVisibility(View.INVISIBLE);

        }

        checkedItem = 0;
    }

    /* 버튼 클릭 이벤트 */


    @Override
    public void onBackPressed() {
        if (!selectMode) {
            moveDirHistory.remove(moveDirHistory.size() - 1);
            if (!moveDirHistory.isEmpty() && moveDirHistory.get(moveDirHistory.size() - 1).equals("\\")) {
                Toast.makeText(this, "마지막 페이지 입니다 \n \'뒤로\'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
            if (moveDirHistory.isEmpty()) {
                finish();
                return;
            }
            moveDirwithoutHistory(moveDirHistory.get(moveDirHistory.size() - 1));
        } else {

        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return false;
    }
}