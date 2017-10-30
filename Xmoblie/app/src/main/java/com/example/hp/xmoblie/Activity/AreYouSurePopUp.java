package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-10-26.
 */

public class AreYouSurePopUp extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_areyousure);

        TextView editText = (TextView) findViewById(R.id.editdata_edit);

        SideStick_BTN commit = (SideStick_BTN)findViewById(R.id.editdata_commit);
        SideStick_BTN re =(SideStick_BTN)findViewById(R.id.editdata_re);

        editText.setText("상호 : "+getIntent().getStringExtra("name")+"\n주소 : "+
        getIntent().getStringExtra("place")+"\n가격 : "+getIntent().getStringExtra("price"));

        editText.setMovementMethod(new ScrollingMovementMethod());

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(200);
                finish();
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(400);
                finish();
            }
        });
    }
}
