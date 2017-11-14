package com.example.hp.xmoblie.Service;

import android.content.Context;

import org.opencv.core.Point;

/**
 * Created by HP on 2017-11-02.
 */

public class ServiceControlCenter {
    private static ServiceControlCenter instance;

    public boolean downloadNow=false;
    private String token;
    private DownloadManagerService downloadManagerService;
    private NotificationBarService notificationBarService;
    private Context context;
    private Point conner_l,conner_r;

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

    public void setConner_l(Point conner_l) {
        this.conner_l = conner_l;
    }

    public void setConner_r(Point conner_r) {
        this.conner_r = conner_r;
    }

    public Point getConner_l() {
        return conner_l;
    }

    public Point getConner_r() {
        return conner_r;
    }
}
