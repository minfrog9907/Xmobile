package com.example.hp.xmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button submitLogin = (Button)findViewById(R.id.submitLogin);
        final EditText idEditText,pwEditText;
        idEditText = (EditText)findViewById(R.id.idEditText);
        pwEditText = (EditText)findViewById(R.id.pwEditText);
        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id, pw;
                id = idEditText.getText().toString();
                pw = pwEditText.getText().toString();
                loginProcess(id, pw);
            }
        });

    }

    private void loginProcess(String id, String pw){
        if(id.equals("test") && pw.equals("1234")){
            SharedPreferences autoLogin = getSharedPreferences("autoLogin",0);
            SharedPreferences.Editor editor = autoLogin.edit();
            editor.putBoolean("autoLogin",true);
            editor.commit();
            Toast.makeText(this, "로그인 성공.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));// 액티비티 종료
        }else{
            Toast.makeText(this, "없는 아이디 이거나 틀린 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
