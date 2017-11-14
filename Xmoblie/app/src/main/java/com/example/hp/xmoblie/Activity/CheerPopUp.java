package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HP on 2017-09-20.
 */

public class CheerPopUp extends SettingActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        TextView dialog_date = (TextView) findViewById(R.id.dialog_date);
        TextView dialog_cheer = (TextView) findViewById(R.id.dialog_cheer);

        Button btnClose = (Button) findViewById(R.id.btnClose);

        String[] weekDay = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};

        Calendar calendar = Calendar.getInstance();

        int num = calendar.get(Calendar.DAY_OF_WEEK)-1;

        String today = weekDay[num];
        dialog_date.setText(today);

        SharedPreferences saveText = getSharedPreferences("saveText", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveText.edit();

        switch(today) {
            case "월요일":
                if (monT.equals(newMon) || newMon == null) {
                    dialog_cheer.setText(monT);
                } else {
                    dialog_cheer.setText(newMon);
                }
                break;
            case "화요일":
                if (thuT.equals(newTue) || newTue == null) {
                    dialog_cheer.setText(tueT);
                } else {
                    dialog_cheer.setText(newTue);
                }
                break;
            case "수요일":
                if (wedT.equals(newWed) || newWed == null) {
                    dialog_cheer.setText(wedT);
                } else {
                    dialog_cheer.setText(newWed);
                }
                finish();
                break;
            case "목요일":
                if (thuT.equals(newThu) || newThu == null) {
                    dialog_cheer.setText(thuT);
                } else {
                    dialog_cheer.setText(newThu);
                }
                break;
            case "금요일":
                if (friT.equals(newFri) || newFri == null) {
                    dialog_cheer.setText(friT);
                } else {
                    dialog_cheer.setText(newFri);
                }
                break;
            default:
                break;
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
