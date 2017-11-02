package com.example.hp.xmoblie.Utill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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

    boolean mBounded;

    String path = "";
    String token = "";
    String filename = "";

    ApiClient apiClient;

    DownloadMotherThread dlm;

    NotificationBarService mNotificationBarService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void downloadFile() throws IOException {
        dlm = new DownloadMotherThread();
        dlm.run(type, filename, path, token, offet, length, this);
        bindService(new Intent(DownloadManager.this,NotificationManager.class),mConnection, BIND_AUTO_CREATE);
        mNotificationBarService.startDownload();
    }

    public void dead() {
        if (dlm != null && dlm.isAlive()) {
            dlm.stop();
        }
        stopSelf();

    }
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mNotificationBarService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            NotificationBarService.LocalBinder mLocalBinder = (NotificationBarService.LocalBinder)service;
            mNotificationBarService = mLocalBinder.getServerInstance();
        }
    };




}
