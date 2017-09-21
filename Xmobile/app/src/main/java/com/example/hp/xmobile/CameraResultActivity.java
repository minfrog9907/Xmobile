package com.example.hp.xmobile;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Created by HP on 2017-09-21.
 */

public class CameraResultActivity extends BaseActivity  {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        setTitle("카메라");
        actionBar.setIcon(R.drawable.photocarmera_light);

        setContentView(R.layout.activity_camera_result);
    }
}
