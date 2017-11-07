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
import android.widget.Toast;

import com.example.hp.xmoblie.R;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.util.*;
import java.lang.*;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import static android.R.attr.editorExtras;
import static android.R.attr.maxLength;
import static android.R.attr.start;
import static android.R.attr.value;
import static java.util.Calendar.HOUR;

public class SettingActivity extends Activity {

    TextView tvHour;
    TextView tvMinute;
    TextView tvStartHour;
    TextView tvStartMinute;
    Croller detaSeekBar;
    Button hourUpBtn, hourDownBtn, minuteUpButton, minuteDownBtn, dailyCheerEditBtn, nightThemeBtn, lightThemeBtn, startHourUpBtn, startHourDownBtn, startMinuteUpBtn, startMinuteDownBtn, btnLogOut;
    EditText MondayCheerText, TuesdayCheerText, WednesdayCheerText, ThursdayCheerText, FridayCheerText;

    boolean editOff = true;

    int startHour;
    int endHour;
    int startMinute;
    int endMinute;
    int pg;

    String strSH, strEH, strSM, strEM;

    static String monT;
    static String tueT;
    static String wedT;
    static String thuT;
    static String friT;
    static String newMon;
    static String newTue;
    static String newWed;
    static String newThu;
    static String newFri;

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
        MondayCheerText = (EditText) findViewById(R.id.MondayCheerText);
        TuesdayCheerText = (EditText) findViewById(R.id.TuesCheerText);
        WednesdayCheerText = (EditText) findViewById(R.id.WednesdayCheerText);
        ThursdayCheerText = (EditText) findViewById(R.id.Thursday);
        FridayCheerText = (EditText) findViewById(R.id.FridayCheerText);
        detaSeekBar = (Croller) findViewById(R.id.detaSeekBar);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        strSH = tvStartHour.getText().toString();
        strEH = tvHour.getText().toString();
        strSM = tvStartMinute.getText().toString();
        strEM = tvMinute.getText().toString();

        setPREF(false);

        SharedPreferences saveText = getSharedPreferences("saveText", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveText.edit();
        this.monT = MondayCheerText.getText().toString();
        this.tueT = TuesdayCheerText.getText().toString();
        this.wedT = WednesdayCheerText.getText().toString();
        this.thuT = ThursdayCheerText.getText().toString();
        this.friT = FridayCheerText.getText().toString();

        editor.putString("mon", this.monT);
        editor.putString("tue", this.tueT);
        editor.putString("wed", this.wedT);
        editor.putString("thuT", this.tueT);
        editor.putString("friT", this.friT);
        editor.commit();
        editor.apply();


        detaSeekBar.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                detaSeekBar.setProgress(progress);
                if (progress == 0) {
                    detaSeekBar.setLabel("0 MB");
                } else if (progress < 1024) {
                    detaSeekBar.setLabel(String.valueOf(progress) + " MB");
                } else {
                    detaSeekBar.setLabel("1 GB");
                }

                pg = detaSeekBar.getProgress();

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

                setTime(false, true, true);

            }
        });

        hourDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTime(false, true, false);

            }
        });

        minuteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(false, false, true);

            }
        });

       minuteDownBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               setTime(false, false, false);

           }
       });

        startHourUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(true, true, true);

            }
        });

        startHourDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(true, true, false);

            }
        });

        startMinuteUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(true, false, true);

            }
        });

        startMinuteDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(true, false, false);

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

                    newMon = MondayCheerText.getText().toString();
                    newThu = TuesdayCheerText.getText().toString();
                    newWed = WednesdayCheerText.getText().toString();
                    newThu = ThursdayCheerText.getText().toString();
                    newFri = FridayCheerText.getText().toString();

                    SharedPreferences saveText = getSharedPreferences("saveText", MODE_PRIVATE);

                }

                SharedPreferences saveText = getSharedPreferences("saveText", MODE_PRIVATE);
                SharedPreferences.Editor editor = saveText.edit();
                editor.putString("Nmon", newMon);
                editor.putString("Ntue", TuesdayCheerText.getText().toString());
                editor.putString("Nwed", WednesdayCheerText.getText().toString());
                editor.putString("Nthu", TuesdayCheerText.getText().toString());
                editor.putString("Nfri", FridayCheerText.getText().toString());
                editor.commit();
                editor.apply();

                monT = saveText.getString("mon", "");
                tueT = saveText.getString("tue", "");
                wedT = saveText.getString("wed", "");
                thuT = saveText.getString("thu", "");
                friT = saveText.getString("fri", "");

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences autoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = autoLogin.edit();

                editor1.putBoolean("autoLogin", false);
                editor1.commit();

                Intent goToLogin = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(goToLogin);
                finish();
            }
        });

    }

    public void setTime(boolean start, boolean hour, boolean increase) {

        if (start && hour == true) {
            if (increase == true) {
                if(startHour < 9) {
                    startHour += 1;
                    tvStartHour.setText("0" + startHour);
                } else if (startHour >= 9 && startHour < 23) {
                    startHour += 1;
                    tvStartHour.setText("" + startHour);
                } else {
                    startHour = 0;
                    tvStartHour.setText("0" + startHour);
                }
            } else {
                if (startHour < 1) {
                    startHour = 23;
                    tvStartHour.setText("" + startHour);
                }
                else if (startHour <= 10) {
                    startHour -= 1;
                    tvStartHour.setText("0" + startHour);
                } else {
                    startHour -= 1;
                    tvStartHour.setText("" + startHour);
                }
            }
        } else if (start == false && hour == true) {
            if (increase == true) {
                if(endHour < 9) {
                    endHour += 1;
                    tvHour.setText("0" + endHour);
                } else if (endHour >= 9 && endHour < 23) {
                    endHour += 1;
                    tvHour.setText("" + endHour);
                } else {
                    endHour = 0;
                    tvHour.setText("0" + endHour);
                }
            } else {
                if (endHour < 1) {
                    endHour = 23;
                    tvHour.setText("" + endHour);
                }
                else if (endHour <= 10) {
                    endHour -= 1;
                    tvHour.setText("0" + endHour);
                } else {
                    endHour -= 1;
                    tvHour.setText("" + endHour);
                }
            }
        } else if (start == true && hour == false) {
            if (increase == true) {
                if(startMinute < 9) {
                    startMinute += 1;
                    tvStartMinute.setText("0" + startMinute);
                } else if (startMinute >= 9 && startMinute < 59) {
                    startMinute += 1;
                    tvStartMinute.setText("" + startMinute);
                } else {
                    startMinute = 0;
                    tvStartMinute.setText("0" + startMinute);
                }
            } else {
                if (startMinute < 1) {
                    startMinute = 59;
                    tvStartMinute.setText("" + startMinute);
                }
                else if (startMinute <= 10) {
                    startMinute -= 1;
                    tvStartMinute.setText("0" + startMinute);
                } else {
                    startMinute -= 1;
                    tvStartMinute.setText("" + startMinute);
                }
            }
        } else {
            if (increase == true) {
                if(endMinute < 9) {
                    endMinute += 1;
                    tvMinute.setText("0" + endMinute);
                } else if (endMinute >= 9 && endMinute < 59) {
                    endMinute += 1;
                    tvMinute.setText("" + endMinute);
                } else {
                    endMinute = 0;
                    tvMinute.setText("0" + endMinute);
                }
            } else {
                if (endMinute < 1) {
                    endMinute = 59;
                    tvMinute.setText("" + endMinute);
                }
                else if (endMinute <= 10) {
                    endMinute -= 1;
                    tvMinute.setText("0" + endMinute);
                } else {
                    endMinute -= 1;
                    tvMinute.setText("" + endMinute);
                }
            }
        }
    }

    public void setPREF(boolean set){
        SharedPreferences setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        if (set == true) {
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("startHour", startHour);
            editor.putInt("endHour", endHour);
            editor.putInt("startMinute", startMinute);
            editor.putInt("endMinute", endMinute);
            editor.putInt("dProgress", pg);
            editor.commit();
        } else {

            int SH = setting.getInt("startHour", 0);
            this.startHour = SH;
            if (this.startHour < 10) {
                tvStartHour.setText("0" + SH);
            } else {
                tvStartHour.setText("" + SH);
            }
            int EH = setting.getInt("endHour", 0);
            this.endHour = EH;
            if (this.endHour < 10) {
                tvHour.setText("0" + EH);
            } else {
                tvHour.setText("" + EH);
            }
            int SM = setting.getInt("startMinute", 0);
            this.startMinute = SM;
            if (this.startMinute < 10) {
                tvStartMinute.setText("0" + SM);
            } else {
                tvStartMinute.setText("" + SM);
            }
            int EM = setting.getInt("endMinute", 0);
            this.endMinute = EM;
            if (this.endMinute < 10) {
                tvMinute.setText("0" + EM);
            } else {
                tvMinute.setText("" + EM);
            }
            int prog = setting.getInt("dProgress", pg);
            this.pg = prog;
            detaSeekBar.setProgress(pg);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        setPREF(true);

    }
}