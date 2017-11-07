package com.example.hp.xmoblie.Utill;

import android.os.Handler;
import android.os.Message;

import com.example.hp.xmoblie.Service.NotificationBarService;

/**
 * Created by HP on 2017-11-06.
 */

public class NotificationHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case  1:
            ServiceControlCenter.getInstance().getNotificationBarService().startDownload();
            break;
            case 200:
                ServiceControlCenter.getInstance().getNotificationBarService().setDownLoadMax(msg.arg1);
                break;
            case 100:
                ServiceControlCenter.getInstance().getNotificationBarService().updateDownload(msg.arg1);
                break;
        }
    }
}
