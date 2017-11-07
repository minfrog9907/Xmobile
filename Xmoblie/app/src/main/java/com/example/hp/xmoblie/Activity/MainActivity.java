package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Custom.Main_BTN;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Utill.NotificationBarService;
import com.example.hp.xmoblie.Utill.ServiceControlCenter;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

            Intent intent = new Intent(MainActivity.this, CheerPopUp.class);
            startActivity(intent);

        } else {
            this.todayDate = date.getString("today", "");
            if (this.todayDate.equals(this.date2AsString)) {
                if (this.isOpen == true) {
                    Intent intent = new Intent(MainActivity.this, CheerPopUp.class);
                    startActivity(intent);

                    this.isOpen = false;
                    this.isFirst = false;
                }
            } else {
                editor.remove("today");
                editor.putString("today", this.date2AsString);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, CheerPopUp.class);
                startActivity(intent);
            }
        }

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

        offWorkProgressClass();

        Intent intent =new Intent(MainActivity.this, NotificationBarService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);


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



    @Override
    public void onBackPressed() {
        doubleCloseHandler.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        offWorkProgressClass();
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
            Log.e("connected","success");
            NotificationBarService.LocalBinder mLocalBinder = (NotificationBarService.LocalBinder)service;
            ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
            serviceControlCenter.setNotificationBarService(mLocalBinder.getServerInstance());

        }
    };
}

