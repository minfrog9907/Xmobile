package com.example.hp.xmoblie.Utill;

import android.content.Context;

import com.example.hp.xmoblie.Activity.MainActivity;
import com.example.hp.xmoblie.Service.DownloadManagerService;
import com.example.hp.xmoblie.Service.NotificationBarService;

/**
 * Created by HP on 2017-11-02.
 */

public class ServiceControlCenter {
    private static ServiceControlCenter instance;

    public boolean downloadNow=false;
    String token;
    DownloadManagerService downloadManagerService;
    NotificationBarService notificationBarService;
    Context context;

    public static ServiceControlCenter getInstance() {
        if (instance == null)
            return instance = new ServiceControlCenter();
        else
            return instance;
    }

    public void setDownloadManagerService(DownloadManagerService downloadManagerService) {
        this.downloadManagerService = downloadManagerService;
    }

    public void setNotificationBarService(NotificationBarService notificationBarService) {
        this.notificationBarService = notificationBarService;
    }

    public DownloadManagerService getDownloadManagerService() {
        return downloadManagerService;
    }

    public NotificationBarService getNotificationBarService() {
        return notificationBarService;
    }

    public void downloadFinish(){
        downloadNow=false;
    }
    public void downloadStart(){downloadNow = true;}
    public boolean isAbleDownload(){
        return downloadNow;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
