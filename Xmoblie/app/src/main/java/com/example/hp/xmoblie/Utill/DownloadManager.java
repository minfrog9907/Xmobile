package com.example.hp.xmoblie.Utill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import java.io.IOException;

/**
 * Created by HP on 2017-10-30.
 */

public class DownloadManager extends Service {
    int length = 0;
    int left;
    int type = 1;

    long offet = 4096;

    String path = "";
    String token = "";
    String filename = "";

    ApiClient apiClient;

    DownloadMotherThread dlm;
    IBinder mBinder = new DownloadManager.LocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiClient = ApiClient.severService;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        filename = intent.getStringExtra("filename");
        path = intent.getStringExtra("path");
        token = intent.getStringExtra("token");
        offet = intent.getLongExtra("offset", 0);
        length = intent.getIntExtra("length", 4000);
        type = intent.getIntExtra("type", 1);
        left = length;

        try {
            downloadFile();
            ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
            serviceControlCenter.getNotificationBarService().startDownload();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void downloadFile() throws IOException {
        dlm = new DownloadMotherThread();
        dlm.run(type, filename, path, token, offet, length, this);
    }

    public void dead() {
        if (dlm != null && dlm.isAlive()) {
            dlm.stop();
        }
        stopSelf();

    }

    public class LocalBinder extends Binder {
        public DownloadManager getServerInstance() {
            return DownloadManager.this;
        }
    }



}
