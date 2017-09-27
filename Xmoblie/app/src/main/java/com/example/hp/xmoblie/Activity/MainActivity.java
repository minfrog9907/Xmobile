package com.example.hp.xmoblie.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.hp.xmoblie.R;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    WebView mWebView;
    private DoubleCloseHandler doubleCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doubleCloseHandler = new DoubleCloseHandler(this);

    }

    @Override
    public void onBackPressed() {
        doubleCloseHandler.onBackPressed();
    }
}

