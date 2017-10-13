package com.example.hp.xmoblie.Activity;

/**
 * Created by Kim doyeon on 2017-09-26.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.xmoblie.R;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.util.*;
import java.lang.*;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import static android.R.attr.start;
import static android.R.attr.value;
import static java.util.Calendar.HOUR;

public class SettingActivity extends Activity {


    TextView tvHour, tvMinute, tvStartHour, tvStartMinute, tvSelectDeta;
    Croller detaSeekBar;
    Button hourUpBtn, hourDownBtn, minuteUpButton, minuteDownBtn, dailyCheerEditBtn, nightThemeBtn, lightThemeBtn, startHourUpBtn, startHourDownBtn, startMinuteUpBtn, startMinuteDownBtn;
    EditText MondayCheerText, TuesdayCheerText, WednesdayCheerText, ThursdayCheerText, FridayCheerText;


    // 출 퇴근

    int hour;
    int minute;
    int startHour;
    int startMinute;

    int progress;

    boolean editOff = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        tvHour = (TextView) findViewById(R.id.tvHour);
        tvMinute = (TextView) findViewById(R.id.tvMinute);
        tvStartHour = (TextView) findViewById(R.id.tvStartHour);
        tvStartMinute = (TextView) findViewById(R.id.tvStartMinute);
        hourUpBtn = (Button) findViewById(R.id.hourUpBtn);
        hourDownBtn = (Button) findViewById(R.id.hourDownBtn);
        startHourUpBtn = (Button) findViewById(R.id.startHourUpBtn);
        startHourDownBtn = (Button) findViewById(R.id.startHourDownBtn);
        startMinuteUpBtn = (Button) findViewById(R.id.startMinuteUpBtn);
        startMinuteDownBtn = (Button) findViewById(R.id.startMinuteDownBtn);
        minuteUpButton = (Button) findViewById(R.id.minuteUpButton);
        minuteDownBtn = (Button) findViewById(R.id.minuteDownBtn);
        dailyCheerEditBtn = (Button) findViewById(R.id.dailyCheerEditBtn);
        nightThemeBtn = (Button) findViewById(R.id.nightThemeBtn);
        lightThemeBtn = (Button) findViewById(R.id.lightThemeBtn);
        MondayCheerText = (EditText) findViewById(R.id.MondayCheerText);
        TuesdayCheerText = (EditText) findViewById(R.id.TuesCheerText);
        WednesdayCheerText = (EditText) findViewById(R.id.WednesdayCheerText);
        ThursdayCheerText = (EditText) findViewById(R.id.Thursday);
        FridayCheerText = (EditText) findViewById(R.id.FridayCheerText);
        detaSeekBar = (Croller) findViewById(R.id.detaSeekBar);
        //tvSelectDeta = (TextView) findViewById(R.id.tvSelectDeta);




        // 출 퇴근 시간 설정

        hour = Integer.parseInt(tvHour.getText().toString());
        minute = Integer.parseInt(tvMinute.getText().toString());

        startHour = Integer.parseInt(tvStartHour.getText().toString());
        startMinute = Integer.parseInt(tvMinute.getText().toString());

        progress = detaSeekBar.getProgress();

        detaSeekBar.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                detaSeekBar.setProgress(progress);

                setDeta(progress);
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {

            }
        });

        hourUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeSetting(true, true);

            }
        });

        hourDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeSetting(true, false);

            }
        });

        minuteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeSetting(false, true);

            }
        });

       minuteDownBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

            timeSetting(false, false);

           }
       });

        startHourUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimeSetting(true, true);

            }
        });

        startHourDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimeSetting(true, false);

            }
        });

        startMinuteUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimeSetting(false, true);

            }
        });

        startMinuteDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimeSetting(false, false);

            }
        });

        dailyCheerEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editOff == true) {

                    MondayCheerText.setEnabled(true);
                    TuesdayCheerText.setEnabled(true);
                    WednesdayCheerText.setEnabled(true);
                    ThursdayCheerText.setEnabled(true);
                    FridayCheerText.setEnabled(true);

                    dailyCheerEditBtn.setBackgroundResource(R.drawable.checked_light);

                    editOff = false;

                } else {

                    MondayCheerText.setEnabled(false);
                    TuesdayCheerText.setEnabled(false);
                    WednesdayCheerText.setEnabled(false);
                    ThursdayCheerText.setEnabled(false);
                    FridayCheerText.setEnabled(false);

                    dailyCheerEditBtn.setBackgroundResource(R.drawable.upload);

                    editOff = true;

                }

            }
        });

    }

    public void timeSetting(boolean setHour, boolean setIncrease) {

        if (setHour && setIncrease == true) {

            if (hour < 9) {

                hour = hour + 1;
                tvHour.setText("0" + hour);

            } else if (hour >= 9 && hour <= 22) {

                hour = hour + 1;
                tvHour.setText("" + hour);

            } else {

                hour = 0;
                tvHour.setText("0" + hour);

            }

        } else if (setHour == true && setIncrease == false) {

            if (hour == 0) {

                hour = 23;
                tvHour.setText("" + hour);

            } else if (hour <= 10) {

                hour = hour - 1;
                tvHour.setText("0" + hour);

            } else {

                hour = hour - 1;
                tvHour.setText("" + hour);

            }

        } else if (setHour == false && setIncrease == true) {

            if (minute < 9) {

                minute = minute + 1;
                tvMinute.setText("0" + minute);

            } else if (minute >= 9 && minute <= 58) {

                minute = minute + 1;
                tvMinute.setText("" + minute);

            } else {

                minute = 0;
                tvMinute.setText("0" + minute);

            }

        } else {

            if (minute == 0) {

                minute = 59;
                tvMinute.setText("" + minute);

            } else if (minute <= 10) {

                minute = minute - 1;
                tvMinute.setText("0" + minute);

            } else {

                minute = minute - 1;
                tvMinute.setText("" + minute);

            }

        }

    }

    public void startTimeSetting(boolean setHour, boolean setIncrease) {

        if (setHour && setIncrease == true) {

            if (startHour < 9) {

                startHour = startHour + 1;
                tvStartHour.setText("0" + startHour);

            } else if (startHour >= 9 && startHour <= 22) {

                startHour = startHour + 1;
                tvStartHour.setText("" + startHour);

            } else {

                startHour = 0;
                tvStartHour.setText("0" + startHour);

            }

        } else if (setHour == true && setIncrease == false) {

            if (startHour == 0) {

                startHour = 23;
                tvStartHour.setText("" + startHour);

            } else if (startHour <= 10) {

                startHour = startHour - 1;
                tvStartHour.setText("0" + startHour);

            } else {

                startHour = startHour - 1;
                tvStartHour.setText("" + startHour);

            }

        } else if (setHour == false && setIncrease == true ) {

            if (startMinute < 9) {

                startMinute = startMinute + 1;
                tvStartMinute.setText("0" + startMinute);

            } else if (startMinute >= 9 && startMinute <= 58) {

                startMinute = startMinute + 1;
                tvStartMinute.setText("" + startMinute);

            } else {

                startMinute = 0;
                tvStartMinute.setText("0" + startMinute);

            }

        } else {

            if (startMinute == 0) {

                startMinute = 59;
                tvStartMinute.setText("" + startMinute);

            } else if (startMinute <= 10) {

                startMinute = startMinute - 1;
                tvStartMinute.setText("0" + startMinute);

            } else {

                startMinute = startMinute - 1;
                tvStartMinute.setText("" + startMinute);

            }

        }

    }

    public void setDeta(float progress){

        if(progress == 0) {
            tvSelectDeta.setText("0 MB");
        } else if (progress < 1024) {
            tvSelectDeta.setText(progress + " MB");
        } else {
            tvSelectDeta.setText("1.0 GB");
        }

    }

}