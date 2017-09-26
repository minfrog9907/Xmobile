package com.example.hp.xmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText idEditText,pwEditText;
        String id, pw;
        idEditText = (EditText)findViewById(R.id.idEditText);
        pwEditText = (EditText)findViewById(R.id.pwEditText);
        id = idEditText.getText().toString();
        pw = pwEditText.getText().toString();
        loginProcess(id, pw);

    }

    private void loginProcess(String id, String pw){
        Log.d("login",id + " " + pw);
    }
}
