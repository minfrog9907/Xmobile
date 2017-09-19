package com.example.hp.xmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    private DoubleCloseHandler doubleCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doubleCloseHandler = new DoubleCloseHandler(this);

        mWebView = (WebView) findViewById(R.id.main_webView);
        mWebView.loadUrl("http://www.google.com");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }else{
                        MainActivity.this.onBackPressed();
                    }
                    return true;
                }

                return false;
            }

        });
    }

    @Override
    public void onBackPressed() {
        doubleCloseHandler.onBackPressed();
    }
}
