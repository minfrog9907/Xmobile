package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Custom.Main_BTN;
import com.example.hp.xmoblie.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    private ArcProgress offWorkProgress;
    private TextView offWorkTimeTxt;
    private DoubleCloseHandler doubleCloseHandler;
    private Main_BTN fileManagerBtn, historyBtn, cameraBtn, settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date date1 = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String date1AsString = dateFormat.format(date1);

        calendar = Calendar.getInstance();
        Date date2 = calendar.getTime();

        String date2AsString = dateFormat.format(date2);

        SharedPreferences date = getSharedPreferences("date", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = date.edit();
        editor.putString("today", date1AsString);
        editor.commit();

        String todayDate = date.getString("today", "");

        if (date2AsString.equals(todayDate)) {
            Intent intent = new Intent(MainActivity.this, CheerPopUp.class);
            startActivity(intent);
        } else {
            editor.putString("today", date2AsString);
            editor.commit();

            Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
        }

        offWorkProgress = (ArcProgress)findViewById(R.id.offWorkProgress);
        offWorkTimeTxt = (TextView)findViewById(R.id.offWorkTime);
        fileManagerBtn = (Main_BTN)findViewById(R.id.fileManagerBtn);
        settingBtn = (Main_BTN)findViewById(R.id.settingBtn);
        cameraBtn =(Main_BTN)findViewById(R.id.cameraBtn);

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
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        offWorkProgressClass();
    }

    public void offWorkProgressClass(){

        SharedPreferences setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        int startHour = setting.getInt("startHour", 0);
        int endHour = setting.getInt("endHour", 0);
        int startMinute = setting.getInt("startMinute", 0);
        int endMinute = setting.getInt("endMinute", 0);

        final Handler handler = new Handler();

        final int onWorkTime = 32400;
        final int offWorkTime = 61200;
        final int workingTime = offWorkTime - onWorkTime;

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
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



}

