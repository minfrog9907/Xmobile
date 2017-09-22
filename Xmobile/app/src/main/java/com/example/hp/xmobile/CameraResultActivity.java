package com.example.hp.xmobile;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;


/**
 * Created by HP on 2017-09-21.
 */

public class CameraResultActivity extends ActionBarActivity {
    public LinearLayout thumnail;
    ImageView preview;
    public ImageView[] thumnailImages = new ImageView[5];
    public int cnt=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        ActionBar actionBar = getSupportActionBar();

        thumnail = (LinearLayout)findViewById(R.id.cameraResult_thumnail);

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_camera, null);
        actionBar.setCustomView(mCustomView);

        preview  = (ImageView)findViewById(R.id.cameraResult_Image);


        Glide.with(this).load(getIntent().getStringExtra("node")).into(preview);

        LinearLayout edit = (LinearLayout)findViewById(R.id.cameraResult_ChangeNode);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UCrop.Options options = new UCrop.Options();
                options.setToolbarTitle("영수증 편집");
                options.setStatusBarColor(Color.parseColor("#1b1b1b"));
                options.setToolbarColor(Color.parseColor("#1f1f1f"));
                options.setActiveWidgetColor(Color.parseColor("#73F1E2"));
                options.setDimmedLayerColor(Color.parseColor("#262626"));
                options.setRootViewBackgroundColor(Color.parseColor("#1b1b1b"));
                options.setToolbarWidgetColor(Color.parseColor("#73F1E2"));
                options.setFreeStyleCropEnabled(true);
                options.useSourceImageAspectRatio();
                options.setLogoColor(Color.parseColor("#73F1E2"));

                UCrop.of(Uri.fromFile(new File(getIntent().getStringExtra("node"))),Uri.fromFile(new File(getIntent().getStringExtra("dir")+"/cuts/"+getIntent().getStringExtra("filename")+cnt+".jpg")))
                        .withOptions(options)
                        .start(CameraResultActivity.this);

            }
        });
        //Log.e("dir",getIntent().getStringExtra("node")+"   "+getIntent().getStringExtra("dir")+"/tmp/");

        LinearLayout backBtn = (LinearLayout) mCustomView.findViewById(R.id.action_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            thumnailImages[cnt] = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            params.rightMargin= 20;
            thumnailImages[cnt].setLayoutParams(params);
            Glide.with(getApplicationContext()).load(resultUri).into(thumnailImages[cnt]);

            thumnail.addView(thumnailImages[cnt++]);

            if(cnt==1){
                preview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,8));
                thumnail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,2));
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}
