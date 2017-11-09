package com.example.hp.xmoblie.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AbsListView.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hp.xmoblie.Adapter.FileManagerListAdapter;
import com.example.hp.xmoblie.Animation.ResizeAnimation;
import com.example.hp.xmoblie.Custom.CustomFilemanagerBtn;
import com.example.hp.xmoblie.Custom.CustomFilemanagerBtnGroup;
import com.example.hp.xmoblie.Dialog.CreateDialogFragment;
import com.example.hp.xmoblie.Dialog.InputListener;
import com.example.hp.xmoblie.Dialog.MkdirDialogFragment;
import com.example.hp.xmoblie.Dialog.RenameDialogFragment;
import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Items.DeleteItem;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.ScrollView.OverScrollListView;
import com.example.hp.xmoblie.Service.ApiClient;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileManagerActivity extends AppCompatActivity {

    private Spinner spinnerOrder, spinnerSort;
    private LinearLayout spinnerList, searchBtn, noFIleTxt;
    private ImageView showSortWay;
    private AutoCompleteTextView searchEdit;
    private OverScrollListView expListView;
    private List<FileItem> listDataHeader;
    private HashMap<FileItem, List<FileItem>> listDataChild;
    private int orderData = 0, sortData = 0, orderCheck = 0, sortCheck = 0;
    private String searchData = "\\";
    private boolean selectMode = false, startFileProtocal = false;
    private CustomFilemanagerBtnGroup cfbg;
    private int cfbgHeight;
    private FileManagerListAdapter listAdapter;
    private ScrollView fileListScroll;
    private FloatingActionButton takePhotoBtn, uploadFileBtn, makeFolderBtn;

    private ApiClient apiClient;

    private List<String> searchHistory = new ArrayList<String>();
    private List<String> moveDirHistory = new ArrayList<String>();
    private List<FileItem> checkedItems = new ArrayList<FileItem>();

    private static final MediaType JSON = MediaType.parse("text/plain");

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

        apiClient = ApiClient.service;
        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        spinnerList = (LinearLayout) findViewById(R.id.spinnerList);
        searchBtn = (LinearLayout) findViewById(R.id.searchBtn);
        noFIleTxt = (LinearLayout) findViewById(R.id.noFIleTxt);
        showSortWay = (ImageView) findViewById(R.id.showSortWay);
        searchEdit = (AutoCompleteTextView) findViewById(R.id.searchEdit);
        expListView = (OverScrollListView) findViewById(R.id.fileList);
        cfbg = (CustomFilemanagerBtnGroup) findViewById(R.id.cfbg);
        fileListScroll = (ScrollView) findViewById(R.id.fileListScroll);
        takePhotoBtn = (FloatingActionButton) findViewById(R.id.takePhotoBtn);
        uploadFileBtn = (FloatingActionButton) findViewById(R.id.uploadFileBtn);
        makeFolderBtn = (FloatingActionButton) findViewById(R.id.makeFolderBtn);
        ViewTreeObserver observer = cfbg.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cfbg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                cfbgHeight = cfbg.getMeasuredHeight();
                cfbg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            }
        });

        Toast.makeText(this, "뒤로가기 버튼을 꾹 누르면 파일 \n매니저가 종료됩니다.", Toast.LENGTH_SHORT).show();
        getSearchHistroy();
        moveDir(searchData);
        spinnerArray();
        addBtnEvent(cfbg.getBtns());

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

        //ListView onClickListener
        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileItemHolder fileItemHolder = (FileItemHolder) view.getTag();
                if (!selectMode) {
                    if (fileItemHolder.fileIcon.getTag().equals("file")) {
                        Toast.makeText(FileManagerActivity.this, "Open File", Toast.LENGTH_SHORT).show();
                    } else {
                        FileItem parants = (FileItem) expListView.getAdapter().getItem(i);
                        searchData = checkRoot() + parants.getFilename();
                        moveDir(searchData);
                    }
                } else {
                    selectItem(fileItemHolder);
                }

            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!selectMode) {
                    changeSelectMode();
                    selectItem((FileItemHolder) view.getTag());
                    adapterView.setSelection(i);
                }
                return true;
            }
        });

        expListView.setOverScrollListener(new OverScrollListView.OverScrolledListener() {
            @Override
            public void overScrolled(int scrollY, int maxY, boolean exceededOffset, boolean didFinishOverScroll) {
                if (exceededOffset) {
                    searchFile(searchData);
                }
            }
        });

        Drawable d = new BitmapDrawable(getResources(), "ic_stat_name");
        expListView.setOverscrollHeader(d);
        expListView.setOverScrollOffsetY(100);

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

        /* Floating button 클릭 이벤트 */
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FileManagerActivity.this, CameraActivity.class).putExtra("token", getIntent().getStringExtra("token")));
            }
        });

        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        makeFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchData == "\\") {
                    Toast.makeText(FileManagerActivity.this, "루트 디렉토리에는 파일을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    createDialog("mkdir");
                }
            }
        });

    }

    /* 경로 이동 및 파일 실행 */

    private void fileProtocal(String path) {
        if (startFileProtocal) return;
        startFileProtocal = true;
        listDataHeader = new ArrayList<>();
        final Call<List<FileItem>> call = apiClient.repoFileNodes(getIntent().getStringExtra("token"), path);
        call.enqueue(new Callback<List<FileItem>>() {
            @Override
            public void onResponse(Call<List<FileItem>> call,
                                   Response<List<FileItem>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); ++i) {
                        listDataHeader.add(response.body().get(i));
                    }

                    if (response.body().size() <= 0) {
                        noFIleTxt.setVisibility(View.VISIBLE);
                    } else {
                        noFIleTxt.setVisibility(View.INVISIBLE);
                    }

                    sortData();
                    adaptList();
                }
                if (response.errorBody() != null) {
                    fileProtocal(searchData);
                }
                startFileProtocal = false;
            }

            @Override
            public void onFailure(Call<List<FileItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
                fileProtocal(searchData);
                startFileProtocal = false;
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
        moveDirHistory.add(path);
        searchFile(path);
    }

    private void moveDirwithoutHistory(String path) {
        searchFile(path);
    }

    private void adaptList() {
        if (listAdapter != new FileManagerListAdapter(this, listDataHeader)) {
            listAdapter = new FileManagerListAdapter(this, listDataHeader);
        }
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

    /* 선택 모드 */

    private void changeListMode() {
        selectMode = false;
        controllBtnGroup();
        checkedItems.clear();
        int count = listAdapter.getCount();

        for (int i = 0; i < count; i++) {

            View group = listAdapter.getViewAt(i);
            FileItemHolder groupItemHolder = (FileItemHolder) group.getTag();
            groupItemHolder.checkBox.setChecked(false);
            groupItemHolder.checkBox.setVisibility(View.INVISIBLE);

            if (groupItemHolder.fileIcon.getTag().equals("folder"))
                groupItemHolder.showMoreMenu.setVisibility(View.VISIBLE);

        }
    }

    private void changeSelectMode() {
        selectMode = true;
        controllBtnGroup();
        int count = listAdapter.getCount();

        for (int i = 0; i < count; i++) {
            View group = listAdapter.getViewAt(i);
            FileItemHolder groupItemHolder = (FileItemHolder) group.getTag();
            groupItemHolder.checkBox.setVisibility(View.VISIBLE);

            if (groupItemHolder.fileIcon.getTag().equals("folder"))
                groupItemHolder.showMoreMenu.setVisibility(View.INVISIBLE);
        }
    }

    private void selectItem(FileItemHolder fileItemHolder) {
        fileItemHolder.checkBox.toggle();
        if (fileItemHolder.checkBox.isChecked()) {
            addSelectedItem(fileItemHolder.realFileItem);
        } else {
            removeItem(fileItemHolder.realFileItem);
        }
        changeCBGMod();
    }

    private void controllBtnGroup() {
        ResizeAnimation animation = new ResizeAnimation(cfbg);
        cfbg.setMultiMod(false);
        animation.setDuration(250);
        if (selectMode) {
            animation.setParams(0, cfbgHeight);
        } else {
            animation.setParams(cfbgHeight, 0);
        }
        cfbg.startAnimation(animation);
    }

    private void addSelectedItem(FileItem fileItem) {
        if (!checkedItems.contains(fileItem)) {
            checkedItems.add(fileItem);
        }
    }

    private void removeItem(FileItem fileItem) {
        if (checkedItems.contains(fileItem)) {
            checkedItems.remove(fileItem);
        }
    }

    private void changeCBGMod() {
        if (checkedItems.size() > 1) {
            cfbg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cfbg.setMultiMod(true);
        } else {
            cfbg.setMultiMod(false);
        }
    }

    /*ButtonGroup 클릭 이벤트*/

    private void addBtnEvent(List<View> viewList) {
        int count = viewList.size();
        for (int i = 0; i < count; i++) {
            viewList.get(i).setOnClickListener(CFBOnclick);
        }
    }

    LinearLayout.OnClickListener CFBOnclick = new CustomFilemanagerBtn.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.selectAll:
                    selectAll();
                    break;
                case R.id.shareFile:
                    shareFile();
                    break;
                case R.id.fileLog:
                    fileLog();
                    break;
                case R.id.changeName:
                    changeName();
                    break;
                case R.id.addTag:
                    addTad();
                    break;
                case R.id.fileInfo:
                    fileInfo();
                    break;
                case R.id.deleteFileBtn:
                    removeFile();
                    break;
            }
        }
    };

    private void selectAll() {

        int count = listAdapter.getCount();

        for (int i = 0; i < count; i++) {
            View group = listAdapter.getViewAt(i);
            FileItemHolder groupItemHolder = (FileItemHolder) group.getTag();
            groupItemHolder.checkBox.setChecked(true);
            if (groupItemHolder.checkBox.isChecked()) {
                addSelectedItem(groupItemHolder.realFileItem);
            }
        }
        changeCBGMod();

    }

    private void shareFile() {
        Log.d("clicked button", "share");
    }

    private void fileLog() {
        Log.d("clicked button", "filelog");
    }

    private void changeName() {
        createDialog("renamefile");
    }

    private void addTad() {
        Log.d("clicked button", "addTag");
    }

    private void fileInfo() {
        Log.d("clicked button", "fileinfo");
    }

    private void removeFile() {
        Log.d("clicked button", "removeFile");
        ArrayList<DeleteItem> deleteItemList = new ArrayList<>();
        DeleteItem deleteItem;
        for (int i = 0; i < checkedItems.size(); i++) {
            deleteItem = new DeleteItem();
            String fileName = checkedItems.get(i).getFilename();

            deleteItem.setFilename(fileName);
            deleteItem.setPath(searchData);

            deleteItemList.add(deleteItem);
        }


        Gson gson = new Gson();
        String jsonPlace = gson.toJson(deleteItemList);

        RequestBody requestBody = RequestBody.create(JSON, jsonPlace);

        final Call<ResponseBody> call = apiClient.repoDelete(getIntent().getStringExtra("token"), jsonPlace);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(FileManagerActivity.this, "파일이 정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    changeListMode();
                    searchFile(searchData);
                } else if (response.errorBody() != null) {
                    Toast.makeText(FileManagerActivity.this, "에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FileManagerActivity.this, "뭔데 이거", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FileManagerActivity.this, "쀄일", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /* 파일 관리 메소드 */

    private void mkDir(String dir) {
        final Call<ResponseBody> call = apiClient.repoMkDir(getIntent().getStringExtra("token"), dir, searchData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(FileManagerActivity.this, "폴더가 정상적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FileManagerActivity.this, "폴더 생성 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
                searchFile(searchData);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void renameFile(String newName, String oldName){
        final Call<ResponseBody> call = apiClient.repoRename(getIntent().getStringExtra("token"), oldName, searchData, newName);
        System.out.println(oldName + "     " + searchData + "     "+ newName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(FileManagerActivity.this, "파일명이 정상적으로 변경되었습니다..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FileManagerActivity.this, "파일명 변경 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
                searchFile(searchData);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkFileFormat(String newDir){
        if (newDir != null) {

            if (newDir.length() <= 30) {

                if (newDir.length() + searchData.length() <= 180) {

                    try {

                        String eucKrDir = new String(newDir.getBytes("utf-8"), "euc-kr");
                        if (newDir.equals(eucKrDir) && !eucKrDir.matches(".*[[*]|>|<|:|\"|/|\\\\|[|]|?].*")) {

                            return true;

                        } else {
                            Toast.makeText(FileManagerActivity.this, "폴더명에 들어갈 수 없는 문자가 포함되어 있습니다.", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    }
                } else {

                    Toast.makeText(FileManagerActivity.this, "폴더명 + 경로명은 200자를 넘지 말아야 합니다.", Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(FileManagerActivity.this, "폴더명은 30자를 넘지 말아야 합니다.", Toast.LENGTH_SHORT).show();

            }

        }
        return false;
    }

    private boolean FileManagement(String newDir){
        if(checkFileFormat(newDir)){
            mkDir(newDir);
        }
        return checkFileFormat(newDir);
    }
    private boolean FileManagement(String newName, String oldName){
        if(checkFileFormat(newName)){
            renameFile(newName, oldName);
        }
        return checkFileFormat(newName);
    }

    /* create dialog */

    private void createDialog(String dialogType) {
        CreateDialogFragment dialog = null;
        InputListener inputListener;

        switch (dialogType){
            case "mkdir" :
                inputListener = new InputListener() {
                    @Override
                    public boolean onInputComplete(String newDir) {return FileManagement(newDir);}
                };
                dialog = MkdirDialogFragment.newInstance(inputListener, searchData.replace("\\", "/"));
                break;
            case "renamefile":
                final String oldName = checkedItems != null ? checkedItems.get(0).getFilename() : "";
                inputListener = new InputListener() {
                    @Override
                    public boolean onInputComplete(String newDir) {return FileManagement(newDir, oldName);}
                };
                dialog = RenameDialogFragment.newInstance(inputListener, oldName);
                break;

        }
        if(dialog != null){
            dialog.show(getSupportFragmentManager(), "addDialog");
        }else{
            Toast.makeText(this, "잘못된 요청 입니다.", Toast.LENGTH_SHORT).show();
        }
    }



    /* 디바이스 버튼 클릭 이벤트 */

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
            changeListMode();
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return false;
    }

    public void downloadFinish(){

    }
}