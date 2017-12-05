package com.example.hp.xmoblie.Dialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.test.ServiceTestCase;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.NotificationBarService;
import com.example.hp.xmoblie.Service.ServiceControlCenter;

/**
 * Created by HP on 2017-11-22.
 */

public class DownloadUploadCancelDialog extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cancel);
        SideStick_BTN yes = (SideStick_BTN)findViewById(R.id.cancel_yes);
        SideStick_BTN no = (SideStick_BTN)findViewById(R.id.cancel_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ServiceControlCenter.getInstance().isAbleDownload()){
                    ServiceControlCenter.getInstance().getDownloadManagerService().cancelDownload(getIntent().getStringExtra("filename"));

                }
                if(ServiceControlCenter.getInstance().isUploadNow()){
                    ServiceControlCenter.getInstance().getUploadManagerService().cancelUpload(getIntent().getStringExtra("filename"));
                }
                finish();
            }
        });

    }
}
