package com.example.hp.xmoblie.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.util.Log;

import com.example.hp.xmoblie.Items.DownloadRequestItem;
import com.example.hp.xmoblie.Thread.DownloadMotherThread;

import java.io.IOException;

/**
 * Created by HP on 2017-10-30.
 */

public class DownloadManagerService extends Service {
    int length = 0;
    int type = 1;

    long offet = 4096;

    String path = "";
    String token = "";
    String filename = "";

    ApiClient apiClient;

    DownloadMotherThread dlm;

    IBinder mBinder = new DownloadManagerService.LocalBinder();


    @org.jetbrains.annotations.Nullable
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

        token = intent.getStringExtra("token");


        return super.onStartCommand(intent, flags, startId);
    }


    public boolean downloadFile(DownloadRequestItem dri) throws IOException {
        if (!ServiceControlCenter.getInstance().isAbleDownload()) {

            offet = dri.getOffset();
            length = dri.getLength();
            type = dri.getType();
            filename = dri.getFilename();
            path = dri.getPath();

            dlm = new DownloadMotherThread();
            dlm.run(type, filename, path, token, offet, length, this);
            ServiceControlCenter.getInstance().downloadStart();
            return true;
        } else
            return false;
    }

    public void dead() {
        if (dlm != null && dlm.isAlive()) {
            dlm.interrupt();
            Log.e("kill","kill MT");
        }
        ServiceControlCenter.getInstance().downloadFinish();
        //FilemanagerService.getInstance().downloadFinish();
    }

    public class LocalBinder extends Binder {
        public DownloadManagerService getServerInstance() {
            return DownloadManagerService.this;

        }
    }
}
