package com.example.hp.xmoblie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

