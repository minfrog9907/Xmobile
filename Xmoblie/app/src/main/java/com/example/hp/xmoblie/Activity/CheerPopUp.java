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

import java.util.Calendar;

/**
 * Created by HP on 2017-09-20.
 */

public class CheerPopUp extends SettingActivity {

    boolean open;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        TextView dialog_date = (TextView) findViewById(R.id.dialog_date);
        TextView dialog_cheer = (TextView) findViewById(R.id.dialog_cheer);
        Button closeBtn = (Button) findViewById(R.id.closeBtn);

        SharedPreferences openCheer = getSharedPreferences("openCheer", Activity.MODE_PRIVATE);
        SharedPreferences.Editor cheer = openCheer.edit();


        String[] weekDay = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        Calendar calendar = Calendar.getInstance();
        int num = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String today = weekDay[num];

        dialog_date.setText(today);

        if (today == "월요일") {
            dialog_cheer.setText(MondayCheerText.getText().toString());
            if(this.open == true) {

                Intent todayOpen = new Intent(CheerPopUp.this, MainActivity.class);
                startActivity(todayOpen);
                this.open = false;

            } else {

                this.open = true;

            }
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

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                open = false;

            }
        });

    }

}
