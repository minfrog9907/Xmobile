package com.example.hp.xmoblie.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.hp.xmoblie.Utill.DownloadMotherThread;
import com.example.hp.xmoblie.Utill.ServiceControlCenter;

import java.io.IOException;

/**
 * Created by HP on 2017-10-30.
 */

public class DownloadManagerService extends Service {
    int length = 0;
    int left;
    int type = 1;

    long offet = 4096;

    String path = "";
    String token = "";
    String filename = "";

    ApiClient apiClient;

    DownloadMotherThread dlm;
    IBinder mBinder = new DownloadManagerService.LocalBinder();


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

          //  ServiceControlCenter serviceControlCenter = ServiceControlCenter.getInstance();
          //  serviceControlCenter.getNotificationBarService().startDownload();

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
        public DownloadManagerService getServerInstance() {
            return DownloadManagerService.this;
        }
    }



}
