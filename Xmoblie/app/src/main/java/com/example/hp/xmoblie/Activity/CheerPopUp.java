package com.example.hp.xmoblie.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

import java.util.Calendar;

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

        String[] weekDay = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        Calendar calendar = Calendar.getInstance();
        int num = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String today = weekDay[num];

        dialog_date.setText(today);

        if (today == "월요일") {
            dialog_cheer.setText(MondayCheerText.getText().toString());
        } else if (today == "화요일") {
            dialog_cheer.setText(TuesdayCheerText.getText().toString());
        } else if (today == "수요일") {
            dialog_cheer.setText(WednesdayCheerText.getText().toString());
        } else if (today == "목요일") {
            dialog_cheer.setText(ThursdayCheerText.getText().toString());
        } else if (today == "금요일") {
            dialog_cheer.setText(FridayCheerText.getText().toString());
        } else {
            finish();
        }

    }

}
