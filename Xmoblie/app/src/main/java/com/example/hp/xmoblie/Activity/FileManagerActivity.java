package com.example.hp.xmoblie.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.hp.xmoblie.Adapter.FileManagerListAdapter;
import com.example.hp.xmoblie.Animation.ResizeAnimation;
import com.example.hp.xmoblie.Custom.CustomFilemanagerBtn;
import com.example.hp.xmoblie.Custom.CustomFilemanagerBtnGroup;
import com.example.hp.xmoblie.Dialog.AddTagDialogFragment;
import com.example.hp.xmoblie.Dialog.CreateDialogFragment;
import com.example.hp.xmoblie.Dialog.InputListener;
import com.example.hp.xmoblie.Dialog.MkdirDialogFragment;
import com.example.hp.xmoblie.Dialog.RenameDialogFragment;
import com.example.hp.xmoblie.Dialog.ShowLogDialogFragment;
import com.example.hp.xmoblie.Helper.DBHelper;
import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Items.DeleteItem;
import com.example.hp.xmoblie.Items.FileItem;

import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.ScrollView.OverScrollListView;
import com.example.hp.xmoblie.Service.ApiClient;
import com.example.hp.xmoblie.Service.FilemanagerService;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Utill.HistorySharedPreferenceManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yalantis.ucrop.util.FileUtils.getDataColumn;
import static com.yalantis.ucrop.util.FileUtils.isDownloadsDocument;
import static com.yalantis.ucrop.util.FileUtils.isExternalStorageDocument;
import static com.yalantis.ucrop.util.FileUtils.isMediaDocument;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class FileManagerActivity extends AppCompatActivity {

    private Spinner spinnerOrder, spinnerSort;
    private LinearLayout spinnerList, searchBtn, noFIleTxt;
    private ImageView showSortWay;
    private AutoCompleteTextView searchEdit;
    private OverScrollListView expListView;
    private ArrayList<FileItem> listDataHeader;
    private HashMap<FileItem, List<FileItem>> listDataChild;
    private int orderData = 0, sortData = 0, orderCheck = 0, sortCheck = 0;
    private String searchData = "\\";
    private String displaySearchData = "/";
    private boolean selectMode = false, startFileProtocol = false;
    private CustomFilemanagerBtnGroup cfbg;
    private int cfbgHeight;
    private FileManagerListAdapter listAdapter;
    private ScrollView fileListScroll;
    private FloatingActionMenu material_design_android_floating_action_menu;
    private FloatingActionButton takePhotoBtn, uploadFileBtn, makeFolderBtn;

    private ApiClient apiClient;

    private ArrayList<String> searchHistory = new ArrayList<String>();
    private ArrayList<String> moveDirHistory = new ArrayList<String>();
    private ArrayList<FileItem> checkedItems = new ArrayList<FileItem>();
    private ArrayList<FileItem> historyList = new ArrayList<FileItem>();
    private DBHelper dbHelper;
    private String token = "";
    private Context thisContext = null;


    private static final MediaType JSON = MediaType.parse("text/plain");

    private final static Comparator<FileItem> comparator = new Comparator<FileItem>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(FileItem o, FileItem t1) {
            return collator.compare(o.getFilename(), t1.getFilename());
        }

    };

    private final static Comparator<FileItem> comparatorD = new Comparator<FileItem>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(FileItem o, FileItem t1) {
            if (o.getLastWriteDate().before(t1.getLastWriteDate())) {
                return 1;
            } else if (o.getLastWriteDate().after(t1.getLastWriteDate())) {
                return -1;
            } else {
                return 0;
            }
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
        material_design_android_floating_action_menu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        takePhotoBtn = (FloatingActionButton) findViewById(R.id.takePhotoBtn);
        uploadFileBtn = (FloatingActionButton) findViewById(R.id.uploadFileBtn);
        makeFolderBtn = (FloatingActionButton) findViewById(R.id.makeFolderBtn);
        token = ServiceControlCenter.getInstance().getToken();
        thisContext = this;
        dbHelper = new DBHelper(this, "HISTORY", null, 1);
        if (getIntent().getStringExtra("path") != null) {
            searchData = getIntent().getStringExtra("path");
        }

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
                spinnerList.clearAnimation();
                int wrap = 60;
                ResizeAnimation resizeAnimationq = new ResizeAnimation(spinnerList);
                resizeAnimationq.setDuration(500);

                if (showSortWay.getRotation() == 180) {
                    resizeAnimationq.setParams(wrap, 0);
                    spinnerList.startAnimation(resizeAnimationq);
                    showSortWay.setRotation(0);
                } else {
                    resizeAnimationq.setParams(0, wrap);
                    spinnerList.startAnimation(resizeAnimationq);
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
                    FileItem fileItem = (FileItem) fileItemHolder.realFileItem;

                    if (fileItemHolder.fileIcon.getTag().equals("file")) {
                        HistorySharedPreferenceManager.getInstance().addHistroy(searchData, fileItemHolder.realFileItem);
                        if (viewFile(fileItem) != null) {
                            startActivity(viewFile(fileItem));
                        }
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
                FileItemHolder fileItemHolder = (FileItemHolder) view.getTag();
                FileItem fileItem = (FileItem) fileItemHolder.realFileItem;
                if (!selectMode) {
                    changeSelectMode();
                    selectItem(fileItemHolder);
                    adapterView.setSelection(i);
                    cfbg.isStared(fileItem.getIsShortcut());

                    ImageView imageView = view.findViewById(R.id.fileIcon);

                    // 태그 생성
                    ClipData.Item item = new ClipData.Item(
                            (CharSequence) fileItem.getFilename());

                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData(fileItem.getFilename(), mimeTypes, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    imageView.startDrag(data, // data to be dragged
                            shadowBuilder, // drag shadow
                            imageView, // 드래그 드랍할  Vew
                            0 // 필요없은 플래그
                    );

                }

                return true;

            }
        });

        expListView.setOverScrollListener(new OverScrollListView.OverScrolledListener() {
            @Override
            public void overScrolled(int scrollY, int maxY, boolean exceededOffset, boolean didFinishOverScroll) {
                if (!selectMode) {
                    if (exceededOffset) {
                        searchFile(searchData);
                    }
                }
            }
        });

//        Drawable d = new BitmapDrawable(getResources(), "ic_stat_name");
//        expListView.setOverscrollHeader(d);
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

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (++orderCheck > 1) {
                    sortData = i;
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
                String searchTxt = searchEdit.getText().toString();
                setSearchHistory(searchTxt);
                searchEdit.clearFocus();
                moveDir(searchTxt);
            }
        });

        /* Floating button 클릭 이벤트 */

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FileManagerActivity.this, CameraActivity.class).putExtra("token", token));
            }
        });

        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
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

    /* 프로토콜 */
    class Protocol {
        private String protocol = null;
        private String psearchData = null;
        private ArrayList<DeleteItem> deleteItemList = null;
        private String folderName = null;
        private String newName = null;
        private String oldName = null;
        private String newDir = null;
        private String fileName = null;
        private String newTag = null;
        private int isStar = 2;

        void setDeleteItemList(ArrayList<DeleteItem> deleteItemList) {
            this.deleteItemList = deleteItemList;
        }

        void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        void setSearchData(String searchData) {
            this.psearchData = searchData;
        }

        void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        void setNewName(String newName) {
            this.newName = newName;
        }

        void setOldName(String oldName) {
            this.oldName = oldName;
        }

        void setNewDir(String newDir) {
            this.newDir = newDir;
        }

        void setNewTag(String newTag) {
            this.newTag = newTag;
        }

        void setFileName(String fileName) {
            this.fileName = fileName;
        }

        void activateProtocol() {
            if (protocol != null) {
                switch (protocol) {
                    case "fileProtocol":
                        if (psearchData != null) {
                            fileProtocol(psearchData);
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }
                        break;
                    case "tagFileProtocol":
                        if (psearchData != null) {
                            tagFileProtocol(psearchData);
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }
                        break;
                    case "removeFileProtocol":
                        if (psearchData != null) {
                            if (deleteItemList != null) {
                                removeFileProtocol(deleteItemList);
                            } else {
                                Log.e("Protocol", "you need setDeleteItemList");
                            }
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }

                        break;
                    case "removeFolderProtocol":
                        if (psearchData != null) {
                            if (folderName != null) {
                                removeFolderProtocol(folderName);
                            } else {
                                Log.e("Protocol", "you need setFolderName");
                            }
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }
                        break;
                    case "renameFileProtocol":
                        if (newName != null) {
                            if (oldName != null) {
                                renameFileProtocol(newName, oldName);
                            } else {
                                Log.e("Protocol", "you need setOldName");
                            }
                        } else {
                            Log.e("Protocol", "you need setNewName");
                        }
                        break;
                    case "mkDirProtocol":
                        if (newDir != null) {
                            mkDirProtocol(newDir);
                        } else {
                            Log.e("Protocol", "you need setNewDir");
                        }
                        break;
                    case "addTagProtocol":
                        if (fileName != null) {
                            if (newTag != null) {
                                addTagProtocol(newTag);
                            } else {
                                Log.e("Protocol", "you need setNewTag");
                            }
                        } else {
                            Log.e("Protocol", "you need setFileName");
                        }
                        break;
                    case "addStarProtocol" :
                        if (psearchData != null) {
                            if (fileName != null) {
                                    fileAddStarProtocol();
                            } else {
                                Log.e("Protocol", "you need setFileName");
                            }
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }
                        break;
                    case "delStarProtocol" :
                        if (psearchData != null) {
                            if (fileName != null) {
                                    fileDelStarProtocol();
                            } else {
                                Log.e("Protocol", "you need setFileName");
                            }
                        } else {
                            Log.e("Protocol", "you need setSearchData");
                        }
                        break;
                    default:
                        Log.e("Protocol", " Nonexistent protocol");
                        break;
                }
            } else {
                Log.e("Protocol", " you need setProtocol");
            }
        }

        private void fileProtocol(String path) {
            if (startFileProtocol) return;
            startFileProtocol = true;
            listDataHeader = new ArrayList<>();
            final Call<List<FileItem>> call = apiClient.repoFileNodes(token, path);
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
                        changeListMode();
                    }
                    if (response.errorBody() != null) {
                        fileProtocol(psearchData);
                    }
                    startFileProtocol = false;
                }

                @Override
                public void onFailure(Call<List<FileItem>> call, Throwable t) {
                    Log.e("jsonResponse", "빼애애앵ㄱ");
                    fileProtocol(psearchData);
                    startFileProtocol = false;
                }
            });
        }

        private void tagFileProtocol(String tag) {
            if (startFileProtocol) return;
            startFileProtocol = true;
            listDataHeader = new ArrayList<>();
            tag = tag.replace("#", "");
            final Call<List<FileItem>> call = apiClient.repoFindTag(token, tag);
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
                        changeListMode();
                    }
                    if (response.errorBody() != null) {
                        fileProtocol(psearchData);
                    }
                    startFileProtocol = false;
                }

                @Override
                public void onFailure(Call<List<FileItem>> call, Throwable t) {
                    Log.e("jsonResponse", "빼애애앵ㄱ");
                    fileProtocol(psearchData);
                    startFileProtocol = false;
                }
            });
        }

        private void removeFileProtocol(ArrayList<DeleteItem> deleteItemList) {
            Gson gson = new Gson();
            String jsonPlace = gson.toJson(deleteItemList);

            final Call<ResponseBody> call = apiClient.repoFileDelete(token, jsonPlace);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(FileManagerActivity.this, "파일이 정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        changeListMode();
                        searchFile(psearchData);
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

        private void removeFolderProtocol(final String folderName) {
            final Call<ResponseBody> call = apiClient.repoFolderDelete(token, psearchData, folderName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(FileManagerActivity.this, "'" + folderName + "' 폴더가 정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        changeListMode();
                        searchFile(psearchData);
                    } else if (response.code() == 400) {
                        Toast.makeText(FileManagerActivity.this, "'" + folderName + "' 폴더가 비어있지 않아 삭제가 불가능 합니다.", Toast.LENGTH_SHORT).show();
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

        private void renameFileProtocol(String newName, String oldName) {
            final Call<ResponseBody> call = apiClient.repoRename(token, oldName, psearchData, newName);
            Log.e("rename",oldName+ psearchData+ newName);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(FileManagerActivity.this, "파일명이 정상적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        changeListMode();
                        searchFile(searchData);
                    } else {
                        Toast.makeText(FileManagerActivity.this, "파일명 변경 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void mkDirProtocol(String dir) {
            final Call<ResponseBody> call = apiClient.repoMkDir(token, dir, searchData);
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

        private void addTagProtocol(String newTag) {
            final String showTag = newTag;
            final Call<ResponseBody> call = apiClient.repoAddTag(token, newTag, fileName, searchData);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(FileManagerActivity.this, fileName + "에 " + showTag + " 태그를 성공적으로 추가하였습니다..", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(FileManagerActivity.this, "이미 존재하는 태그입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FileManagerActivity.this, "태그 추가 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    changeListMode();
                    searchFile(searchData);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void fileAddStarProtocol() {
            final Call<ResponseBody> call = apiClient.repoAddShortCut(token, psearchData, fileName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    if (response.body() != null) {
                        Toast.makeText(FileManagerActivity.this, "즐겨찾기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    changeListMode();
                    searchFile(searchData);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void fileDelStarProtocol(){
            final Call<ResponseBody> call = apiClient.repoDelShortCut(token, psearchData, fileName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    if (response.body() != null) {
                        Toast.makeText(FileManagerActivity.this, "즐겨찾기에서 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    changeListMode();
                    searchFile(searchData);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(FileManagerActivity.this, "통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /* 경로 이동 및 파일 실행 */

    private void searchFile(String path) {
        Protocol protocol = new Protocol();
        searchData = path;

        if (searchData == null || searchData.isEmpty() || searchData.equals("")) {
            searchData = "\\";
        } else if (searchData.length() > 1 && (searchData.charAt(searchData.length() - 1) == '/' || searchData.charAt(searchData.length() - 1) == '\\')) {
            searchData = searchData.substring(0, path.length() - 1);
        }

        if (searchData.charAt(0) == '#') {
            protocol.setProtocol("tagFileProtocol");
        } else {
            searchData = searchData.replace("/", "\\");
            searchEdit.setText(searchData.replace("\\", "/"));
            searchEdit.setSelection(searchEdit.length());
            protocol.setProtocol("fileProtocol");
        }

        protocol.setSearchData(searchData);
        protocol.activateProtocol();
    }

    private void moveDir(String path) {
        moveDirHistory.add(path);
        searchFile(path);
    }

    public void moveDirwithoutHistory(String path) {
        searchFile(path);
    }

    private void adaptList() {
        listAdapter = new FileManagerListAdapter(this, listDataHeader);
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
        Comparator<FileItem> comparate = comparator;

        switch (sortData) {
            case 0:
                comparate = comparator;
                break;
            case 1:
                comparate = comparatorD;
                break;

        }

        Collections.sort(listDataHeader, comparate);

        switch (orderData) {
            case 0:
                // no function
                break;
            case 1:
                Collections.reverse(listDataHeader);
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

        if (searchHistory.size() > 100) {
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
        ArrayList<String> urls = new ArrayList<String>();
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
        if (selectMode) {
            selectMode = false;
            toggleFloating(selectMode);
            controlBtnGroup(false);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
    }

    private void changeSelectMode() {
        if (!selectMode) {
            selectMode = true;
            toggleFloating(selectMode);
            controlBtnGroup(true);
            int count = listAdapter.getCount();

            for (int i = 0; i < count; i++) {
                View group = listAdapter.getViewAt(i);
                FileItemHolder groupItemHolder = (FileItemHolder) group.getTag();
                groupItemHolder.checkBox.setVisibility(View.VISIBLE);

                if (groupItemHolder.fileIcon.getTag().equals("folder"))
                    groupItemHolder.showMoreMenu.setVisibility(View.INVISIBLE);
            }
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

    private void controlBtnGroup(boolean showBtnGroup) {
        ResizeAnimation animation = new ResizeAnimation(cfbg);
        cfbg.setMultiMod(false);
        animation.setDuration(250);
        if (showBtnGroup) {
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

    private void toggleFloating(boolean isSelectMod) {
        if (isSelectMod) {
            material_design_android_floating_action_menu.setVisibility(View.GONE);
        } else {
            material_design_android_floating_action_menu.setVisibility(View.VISIBLE);
        }
    }

    /*ButtonGroup 클릭 이벤트*/

    private void addBtnEvent(List<View> viewList) {
        int count = viewList.size();
        for (int i = 0; i < count; i++) {
            View view = viewList.get(i);
            view.setOnClickListener(CFBOnclick);
            view.setOnDragListener(new DragListener());

        }
    }

    LinearLayout.OnClickListener CFBOnclick = new CustomFilemanagerBtn.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!checkedItems.isEmpty()) {
                switch (view.getId()) {
                    case R.id.downloadFile:
                        FilemanagerService.getInstance().downloadFileStart(checkedItems, thisContext, searchData);
                        break;
                    case R.id.shareFile:
                        FilemanagerService.getInstance().shareFile(searchData);
                        break;
                    case R.id.fileLog:
                        showFilelog();
                        break;
                    case R.id.changeName:
                        changeName();
                        break;
                    case R.id.addTag:
                        addTag();
                        break;
                    case R.id.fileInfo:
                        fileInfo();
                        break;
                    case R.id.deleteFileBtn:
                        removeFile();
                        break;
                    case R.id.addStar:
                        addStar();
                        break;
                }
            } else {
                Toast.makeText(FileManagerActivity.this, "해당 작업을 할 파일을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void changeName() {
        createDialog("renameFile");
    }

    private void showFilelog() {
        createDialog("fileLog");
    }

    private void addTag() {
        createDialog("addTag");
    }

    private void removeFile() {
        ArrayList<DeleteItem> deleteItemList = new ArrayList<>();
        ArrayList<String> deleteFolderList = new ArrayList<>();
        DeleteItem deleteItem;

        for (int i = 0; i < checkedItems.size(); i++) {
            FileItem fileItem = checkedItems.get(i);
            if (fileItem.getType() == 128) {
                deleteItem = new DeleteItem();
                String fileName = checkedItems.get(i).getFilename();

                deleteItem.setFilename(fileName);
                deleteItem.setPath(searchData);

                deleteItemList.add(deleteItem);
            } else if (fileItem.getType() == 16) {
                deleteFolderList.add(fileItem.getFilename());
            }
        }
        if (deleteItemList.size() > 0) {
            Protocol protocol = new Protocol();
            protocol.setProtocol("removeFileProtocol");
            protocol.setSearchData(searchData);
            protocol.setDeleteItemList(deleteItemList);
            confirmDialog("파일을 삭제 하시겠습니까?", protocol).show();
        }
        if (deleteFolderList.size() > 0) {
            for (int i = 0; i < deleteFolderList.size(); i++) {
                Protocol protocol = new Protocol();
                protocol.setProtocol("removeFolderProtocol");
                protocol.setSearchData(searchData);
                protocol.setFolderName(deleteFolderList.get(i));
                confirmDialog("'" + deleteFolderList.get(i) + "' 폴더를 삭제 하시겠습니까?", protocol).show();
            }
        }
    }

    private void fileInfo() {
        FileItem fileItem = (FileItem) checkedItems.get(0);
        FilemanagerService.getInstance().fileInfoStart(token, searchData, fileItem, this, getSupportFragmentManager());
    }

    private void addStar(){
        FileItem fileItem = (FileItem) checkedItems.get(0);
        Protocol protocol = new Protocol();
        if(fileItem.getIsShortcut() == 0){
            protocol.setProtocol("addStarProtocol");
            protocol.setSearchData(searchData);
            protocol.setFileName(fileItem.getFilename());
        }else if(fileItem.getIsShortcut() == 1){
            protocol.setProtocol("delStarProtocol");
            protocol.setSearchData(searchData);
            protocol.setFileName(fileItem.getFilename());
        }
        protocol.activateProtocol();
    }
    /* 파일 관리 메소드 */

    private boolean checkFileFormat(String newDir) {
        if (newDir != null) {

            if (newDir.length() <= 30) {

                if (newDir.length() + searchData.length() <= 180) {

                    try {

                        byte[] stringbyte = newDir.getBytes(Charset.forName("euc-kr"));
                        String eucKrDir = new String(stringbyte, "euc-kr");

                        if (newDir.equals(eucKrDir) && !eucKrDir.matches(".*[[*]|>|<|:|\"|/|\\\\|[|]|?].*")) {

                            return true;

                        } else {
                            Toast.makeText(FileManagerActivity.this, "폴더명에 들어갈 수 없는 문자가 포함되어 있습니다.", Toast.LENGTH_SHORT).show();
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

    private boolean FileManagement(String newDir) {
        if (checkFileFormat(newDir)) {
            Protocol protocol = new Protocol();
            protocol.setProtocol("mkDirProtocol");
            protocol.setNewDir(newDir);
            protocol.activateProtocol();
            return true;
        }
        return false;
    }

    private boolean FileManagement(String str1, String str2, String type) {
        Protocol protocol = new Protocol();
        switch (type) {
            case "renameFile":
                if (checkFileFormat(str1)) {
                    protocol.setProtocol("renameFileProtocol");
                    protocol.setSearchData(searchData);
                    protocol.setNewName(str1);
                    protocol.setOldName(str2);
                    protocol.activateProtocol();
                    return true;
                }
                break;
            case "addTag":
                if (checkFileFormat(str1)) {
                    protocol.setProtocol("addTagProtocol");
                    protocol.setSearchData(searchData);
                    protocol.setNewTag(str1);
                    protocol.setFileName(str2);
                    protocol.activateProtocol();
                    return true;
                }
                break;
        }
        return false;
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all files
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1000);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK) {
                    String filePath =getPathFromURI(this,data.getData());
                    File file = new File(filePath);
                    filePath=filePath.replace("/"+file.getName(),"");
                    long file_size = file.length();
                    try {
                        ServiceControlCenter.getInstance().getUploadManagerService().uploadFile(searchData, file.getName(), filePath, file_size);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    System.out.println(
//                            file.getName() + "\n"
//                            + file_size +"\n"
//                            + file.getPath() + "\n"
//                            + file.getAbsolutePath()+ "\n"
//                    );

                }
        }
    }

    /* create dialog */

    private void createDialog(String dialogType) {
        CreateDialogFragment dialog = null;
        InputListener inputListener;

        switch (dialogType) {
            case "mkdir":
                inputListener = new InputListener() {
                    @Override
                    public boolean onInputComplete(String newDir) {
                        return FileManagement(newDir);
                    }
                };
                dialog = MkdirDialogFragment.newInstance(inputListener, searchData.replace("\\", "/"));
                break;
            case "fileLog":
                dialog = ShowLogDialogFragment.newInstance(checkedItems, searchData, token, this);
                break;
            case "renameFile":
                if (checkedItems.get(0).getType() == 128) {
                    final String oldName = checkedItems != null ? checkedItems.get(0).getFilename() : "";
                    inputListener = new InputListener() {
                        @Override
                        public boolean onInputComplete(String newDir) {
                            return FileManagement(newDir, checkedItems.get(0).getDisplayName(), "renameFile");
                        }
                    };
                    dialog = RenameDialogFragment.newInstance(inputListener, oldName);
                }else{
                    Toast.makeText(thisContext, "폴더명은 변경 불가능 합니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case "addTag":
                if (checkedItems.get(0).getType() == 128) {
                    final String fileName = checkedItems != null ? checkedItems.get(0).getFilename() : "";
                    inputListener = new InputListener() {
                        @Override
                        public boolean onInputComplete(String name) {
                            return FileManagement(name, fileName, "addTag");
                        }
                    };
                    dialog = AddTagDialogFragment.newInstance(inputListener, fileName);
                } else {
                    Toast.makeText(this, "태그는 파일에만 추가 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case "fileInfo":

                break;
        }
        if (dialog != null) {
            dialog.show(getSupportFragmentManager(), "addDialog");
        }
    }

    private Dialog confirmDialog(String title, final Protocol protocol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FileManagerActivity.this);
        builder.setMessage(title)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FileManagerActivity.this, "확인됨", Toast.LENGTH_SHORT).show();
                        protocol.activateProtocol();
                    }
                })
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FileManagerActivity.this, "취소됨", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

    private Intent viewFile(FileItem fileItem) {
        String fileName = fileItem.getFilename();

        Context ctx = this;
        Intent fileLinkIntent = new Intent(Intent.ACTION_VIEW);// 뷰형태
        fileLinkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(new File(Environment.getExternalStorageDirectory(), "XMobileDownLoad"), fileName); // 파일 불러옴 여기가 널이면 처리하면될듯  if (file.exists()) 파일유무 확인

        if (!file.exists()) {
            Toast.makeText(ctx, "파일을 다운로드 합니다." , Toast.LENGTH_SHORT).show();
            FilemanagerService.getInstance().downloadFileStart(fileItem, thisContext,searchData);
        } else {
            Toast.makeText(ctx, "파일을 엽니다.", Toast.LENGTH_SHORT).show();

            fileLinkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileLinkIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(this, "com.example.hp.xmoblie.provider", file);
            //확장자 구하기
            String fileExtend = getExtension(file.getAbsolutePath());
            // 파일 확장자 별로 mime type 지정해 준다.
            if (fileExtend.equalsIgnoreCase("mp3")) {
                fileLinkIntent.setDataAndType(uri, "audio/*");
            } else if (fileExtend.equalsIgnoreCase("mp4")) {
                fileLinkIntent.setDataAndType(uri, "video/*");
            } else if (fileExtend.equalsIgnoreCase("jpg")
                    || fileExtend.equalsIgnoreCase("jpeg")
                    || fileExtend.equalsIgnoreCase("gif")
                    || fileExtend.equalsIgnoreCase("png")
                    || fileExtend.equalsIgnoreCase("bmp")) {
                fileLinkIntent.setDataAndType(uri, "image/*");
            } else if (fileExtend.equalsIgnoreCase("txt")) {
                fileLinkIntent.setDataAndType(uri, "text/*");
            } else if (fileExtend.equalsIgnoreCase("doc")
                    || fileExtend.equalsIgnoreCase("docx")) {
                fileLinkIntent.setDataAndType(uri, "application/msword");
            } else if (fileExtend.equalsIgnoreCase("xls")
                    || fileExtend.equalsIgnoreCase("xlsx")) {
                fileLinkIntent.setDataAndType(uri,
                        "application/vnd.ms-excel");
            } else if (fileExtend.equalsIgnoreCase("ppt")
                    || fileExtend.equalsIgnoreCase("pptx")) {
                fileLinkIntent.setDataAndType(uri,
                        "application/vnd.ms-powerpoint");
            } else if (fileExtend.equalsIgnoreCase("pdf")) {
                fileLinkIntent.setDataAndType(uri, "application/pdf");
            } else if (fileExtend.equalsIgnoreCase("hwp")) {
                fileLinkIntent.setDataAndType(uri,
                        "application/haansofthwp");
            }
            PackageManager pm = ctx.getPackageManager();
            List<ResolveInfo> list = pm.queryIntentActivities(fileLinkIntent,
                    PackageManager.GET_META_DATA);
            if (list.size() == 0) {
                Toast.makeText(ctx, fileName + "을 확인할 수 있는 앱이 설치되지 않았습니다.",
                        Toast.LENGTH_SHORT).show();
                return null;
            } else {
                return fileLinkIntent;
            }
        }
        return null;
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

    /* 리스너 */
    class DragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // 이벤트 시작
            switch (event.getAction()) {

                // 이미지를 드래그해서 드랍시켰을때
                case DragEvent.ACTION_DROP:
                    switch (v.getId()) {
                        case R.id.deleteFileBtn:
                            removeFile();
                            break;
                        case R.id.changeName:
                            changeName();
                            break;
                        case R.id.fileLog:

                            break;
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        moveDirwithoutHistory(searchData);
    }
}

