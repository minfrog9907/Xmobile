package com.example.hp.xmoblie.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hp.xmoblie.Custom.Main_BTN;
import com.example.hp.xmoblie.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Calendar;

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

        doubleCloseHandler = new DoubleCloseHandler(this);
        offWorkProgress = (ArcProgress)findViewById(R.id.offWorkProgress);
        offWorkTimeTxt = (TextView)findViewById(R.id.offWorkTime);
        fileManagerBtn = (Main_BTN)findViewById(R.id.fileManagerBtn);
        settingBtn = (Main_BTN)findViewById(R.id.settingBtn);

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

        offWorkProgressClass();
    }

    public void offWorkProgressClass(){
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

