package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.xmoblie.Adapter.ShortCutListAdapter;
import com.example.hp.xmoblie.Custom.Main_BTN;
import com.example.hp.xmoblie.Holder.ShortCutItemHolder;
import com.example.hp.xmoblie.Items.ShortCutItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;
import com.example.hp.xmoblie.Service.DownloadManagerService;
import com.example.hp.xmoblie.Service.NotificationBarService;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Service.UploadManagerService;
import com.example.hp.xmoblie.Utill.DoubleCloseHandler;
import com.example.hp.xmoblie.Utill.SessionCall;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    private ArcProgress offWorkProgress;
    private TextView offWorkTimeTxt;
    private DoubleCloseHandler doubleCloseHandler;
    private Main_BTN fileManagerBtn, historyBtn, cameraBtn, settingBtn;
    private final Handler handler = new Handler();
    private Calendar calendar;
    private static String date1AsString;
    private static String date2AsString;
    private static String todayDate;
    private DateFormat dateFormat;
    private Date date1, date2;
    private SharedPreferences setting;
    private ApiClient apiClient;
    private ListView shortcutlist;
    private String token;

    private static boolean isOpen = true;
    private static boolean isFirst = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        calendar = Calendar.getInstance();
        date2 = calendar.getTime();

        date2AsString = dateFormat.format(date2);
        setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        apiClient = ApiClient.service;
        shortcutlist = (ListView) findViewById(R.id.shortcutlist);

        new SessionCall().SessionCall();

        ServiceControlCenter.getInstance().setLimitData(setting.getInt("dProgress",0));

        SharedPreferences date = getSharedPreferences("date", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = date.edit();
        editor.putBoolean("first", this.isFirst);

        boolean first = date.getBoolean("first", this.isFirst);
        boolean open = date.getBoolean("open", this.isOpen);

        if (first == true) {
            editor.putBoolean("first", this.isFirst);

            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            date1 = calendar.getTime();
            this.date1AsString = dateFormat.format(date1);

            this.isOpen = false;
            this.isFirst = false;

            editor.putString("today", this.date1AsString);
            editor.putBoolean("first", this.isFirst);
            editor.putBoolean("open", this.isOpen);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, CheerDialog.class);
            startActivity(intent);

        } else {
            this.todayDate = date.getString("today", "");
            if (this.todayDate.equals(this.date2AsString)) {
                if (this.isOpen == true) {
                    Intent intent = new Intent(MainActivity.this, CheerDialog.class);
                    startActivity(intent);

                    this.isOpen = false;
                    this.isFirst = false;
                }
            } else {
                editor.remove("today");
                editor.putString("today", this.date2AsString);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, CheerDialog.class);
                startActivity(intent);
            }
        }
        token = ServiceControlCenter.getInstance().getToken();

        /*doubleCloseHandler = new DoubleCloseHandler(this);*/
        doubleCloseHandler = new DoubleCloseHandler(this);
        offWorkProgress = (ArcProgress)findViewById(R.id.offWorkProgress);
        offWorkTimeTxt = (TextView)findViewById(R.id.offWorkTime);
        fileManagerBtn = (Main_BTN)findViewById(R.id.fileManagerBtn);
        settingBtn = (Main_BTN)findViewById(R.id.settingBtn);
        cameraBtn =(Main_BTN)findViewById(R.id.cameraBtn);
        historyBtn = (Main_BTN)findViewById(R.id.historyBtn);

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        fileManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FileManagerActivity.class).putExtra("token",getIntent().getStringExtra("token")));
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class).putExtra("token",getIntent().getStringExtra("token")));
            }
        });

        shortcutlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShortCutItemHolder shortCutItemHolder = (ShortCutItemHolder)view.getTag();
                ShortCutItem shortCutItem = shortCutItemHolder.realShortCutItem;
                String path = "\\";
                if(shortCutItem.getType() == 16){
                    if(shortCutItem.getPath().equals("\\")){
                        path = shortCutItem.getPath() + "\\" + shortCutItem.getFilename();
                    }else{
                        path = shortCutItem.getPath();
                    }
                }else{
                    path = shortCutItem.getPath();
                }
                startActivity(new Intent(MainActivity.this,FileManagerActivity.class).putExtra("path",path));
            }
        });

        offWorkProgressClass();
        bindService(new Intent(MainActivity.this,UploadManagerService.class),mUploadConnection,BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, NotificationBarService.class),mConnection,BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, DownloadManagerService.class),mDownConnection,BIND_AUTO_CREATE);
        shortCutProtocol(token, 0,10);

    }

    public void offWorkProgressClass(){
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                int onWorkTime = (setting.getInt("startHour", 9) * 3600) + (setting.getInt("startMinute", 0) * 60);
                int offWorkTime = (setting.getInt("endHour", 18) * 3600) + (setting.getInt("endMinute", 0) * 60);
                int workingTime = offWorkTime - onWorkTime;

                Calendar oCalendar = Calendar.getInstance( );
                int nowHour = oCalendar.get(Calendar.HOUR_OF_DAY);
                int nowMin = oCalendar.get(Calendar.MINUTE);
                int nowSec = oCalendar.get(Calendar.SECOND);
                int nowMilli = ((nowHour * 3600) + (nowMin * 60) + nowSec);

                if(onWorkTime > nowMilli){
                    nowMilli = onWorkTime;
                    offWorkProgress.setBottomText("출근");
                }else if(offWorkTime < nowMilli){
                    nowMilli = offWorkTime;
                    offWorkProgress.setBottomText("퇴근");
                }else{
                    offWorkProgress.setBottomText("열일중");
                }

                String showSec = nowSec%2 == 0 ? ":":" ";
                String ampm = oCalendar.get(Calendar.AM_PM) != Calendar.SUNDAY ? "AM" : "PM";
                int zeroHour = nowHour >= 12 ? nowHour - 12 : nowHour;
                String zeroMin = nowMin >= 10 ? ""+nowMin : "0"+nowMin;
                offWorkTimeTxt.setText(zeroHour + showSec + zeroMin + " " + ampm);
                offWorkProgress.setProgress((int)(((double) (nowMilli - onWorkTime) / (double) workingTime)*100));

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 0);
    }

    private void shortCutProtocol(final String token, final int offset, final int limit) {
        final ArrayList<ShortCutItem> listDataHeader = new ArrayList<>();
        final Call<List<ShortCutItem>> call = apiClient.repoShortCut(token,offset, limit);
        call.enqueue(new Callback<List<ShortCutItem>>() {
            @Override
            public void onResponse(Call<List<ShortCutItem>> call,
                                   Response<List<ShortCutItem>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); ++i) {
                        listDataHeader.add(response.body().get(i));
                    }
                    Collections.reverse(listDataHeader);
                    makeShortCut(listDataHeader);
                }
            }

            @Override
            public void onFailure(Call<List<ShortCutItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
                shortCutProtocol(token, offset, limit);
            }
        });
    }

    private void makeShortCut(ArrayList<ShortCutItem> listDataHeader){
        ShortCutListAdapter shortCutListAdapter = new ShortCutListAdapter(this, listDataHeader);
        shortcutlist.setAdapter(shortCutListAdapter);
    }


    @Override
    public void onBackPressed() {
        doubleCloseHandler.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        offWorkProgressClass();
        shortCutProtocol(token, 0,10);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(0);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("connected","failed");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("connected","notification success");
            NotificationBarService.LocalBinder mLocalBinder = (NotificationBarService.LocalBinder)service;
            ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
            serviceControlCenter.setNotificationBarService(mLocalBinder.getServerInstance());

        }
    };
    ServiceConnection mDownConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("connected", "failed");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("connected", "download success");
            DownloadManagerService.LocalBinder mLocalBinder = (DownloadManagerService.LocalBinder) service;
            ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
            serviceControlCenter.setDownloadManagerService(mLocalBinder.getServerInstance());

        }
    };
    ServiceConnection mUploadConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("connected", "upload success");
            UploadManagerService.LocalBinder mLocalBinder = (UploadManagerService.LocalBinder) service;
            ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
            serviceControlCenter.setUploadManagerService(mLocalBinder.getServerInstance());

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("connected", "failed");
        }

    };
}

