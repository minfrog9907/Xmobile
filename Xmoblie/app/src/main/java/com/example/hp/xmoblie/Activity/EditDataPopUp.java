package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-10-26.
 */

public class EditDataPopUp extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_editdata);

        final EditText editText = (EditText)findViewById(R.id.editdata_edit);

        SideStick_BTN commit = (SideStick_BTN)findViewById(R.id.editdata_commit);
        SideStick_BTN re =(SideStick_BTN)findViewById(R.id.editdata_re);

        editText.setText(getIntent().getStringExtra("data"));

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(200,new Intent().putExtra("data",editText.getText().toString()));
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
