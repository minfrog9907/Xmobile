package com.example.hp.xmoblie.Activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.hp.xmoblie.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    private ArcProgress offWorkProgress;
    private TextView offWorkTime;
    private DoubleCloseHandler doubleCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doubleCloseHandler = new DoubleCloseHandler(this);
        offWorkProgress = (ArcProgress)findViewById(R.id.offWorkProgress);

        final Handler handler = new Handler();
        final int[] time = {10};
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                offWorkProgress.setProgress(time[0]);
                time[0] = time[0] + 10;
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);
    }



    @Override
    public void onBackPressed() {
        doubleCloseHandler.onBackPressed();
    }
}

