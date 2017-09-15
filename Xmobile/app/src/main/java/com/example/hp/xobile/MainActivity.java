package com.example.hp.xobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplication().setTheme(R.style.AppTheme_White);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
