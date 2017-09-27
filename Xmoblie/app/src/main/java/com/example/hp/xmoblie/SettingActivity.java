package com.example.hp.xmoblie;

/**
 * Created by Kim doyeon on 2017-09-26.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.*;
import java.lang.*;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import static java.util.Calendar.HOUR;

public class SettingActivity extends Activity {

    TextView tvHour;
    TextView tvMinute;
    TextView tvSeekBarDetaView;
    Button hourUpBtn, hourDownBtn, minuteUpButton, minuteDownBtn, dailyCheerEditBtn, nightThemeBtn, lightThemeBtn;
    CircularSeekBar detaSeekBar;
    EditText MondayCheerText, TuesdayCheerText, WednesdayCheerText, ThursdayCheerText, FridayCheerText;

    int hour;
    int minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tvHour = (TextView) findViewById(R.id.tvHour);
        tvMinute = (TextView) findViewById(R.id.tvMinute);
        tvSeekBarDetaView = (TextView) findViewById(R.id.tvSeekBarDetaView);

        hourUpBtn = (Button) findViewById(R.id.hourUpBtn);
        hourDownBtn = (Button) findViewById(R.id.hourDownBtn);
        minuteUpButton = (Button) findViewById(R.id.minuteUpButton);
        minuteDownBtn = (Button) findViewById(R.id.minuteDownBtn);
        dailyCheerEditBtn = (Button) findViewById(R.id.dailyCheerEditBtn);
        nightThemeBtn = (Button) findViewById(R.id.nightThemeBtn);
        lightThemeBtn = (Button) findViewById(R.id.lightThemeBtn);
        detaSeekBar = (CircularSeekBar) findViewById(R.id.detaSeekBar);
        MondayCheerText = (EditText) findViewById(R.id.MondayCheerText);
        TuesdayCheerText = (EditText) findViewById(R.id.TuesCheerText);
        WednesdayCheerText = (EditText) findViewById(R.id.WednesdayCheerText);
        ThursdayCheerText = (EditText) findViewById(R.id.Thursday);
        FridayCheerText = (EditText) findViewById(R.id.FridayCheerText);

        hour = Integer.parseInt(tvHour.getText().toString());
        minute = Integer.parseInt(tvMinute.getText().toString());

        hourUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hour <= 8) {

                    hour = hour + 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("0" + hourNumber);

                } else if (hour == 10) {

                    hour = hour + 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("" + hourNumber);

                } else if (hour <= 23) {

                    hour = hour + 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("" + hourNumber);

                } else {

                    hour = 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("0" + hourNumber);

                }
            }
        });

        hourDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hour >= 11) {

                    hour = hour - 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("" + hourNumber);

                } else if (hour == 10) {

                    hour = hour - 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("0" + hourNumber);

                } else if (hour >= 2) {

                    hour = hour - 1;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("0" + hourNumber);

                } else {

                    hour = 24;

                    String hourNumber = Integer.toString(hour);
                    tvHour.setText("" + hourNumber);
                }

            }
        });

    }

}