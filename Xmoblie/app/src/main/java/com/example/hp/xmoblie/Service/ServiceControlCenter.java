package com.example.hp.xmoblie.Service;

import android.content.Context;

import org.opencv.core.Point;

/**
 * Created by HP on 2017-11-02.
 */

public class ServiceControlCenter {
    private static ServiceControlCenter instance;

    public boolean downloadNow=false, uploadNow =false;
    private String token;
    private UploadManagerService uploadManagerService;
    private DownloadManagerService downloadManagerService;
    private NotificationBarService notificationBarService;
    private Context context;
    private Point conner_l,conner_r;
    private long limitData;

    public ServiceControlCenter() {
    }

    public static ServiceControlCenter getInstance() {
        if (instance == null)
            return instance = new ServiceControlCenter();
        else
            return instance;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public void setLimitData(int limitData) {
        this.limitData = limitData*1048576;
    }

    public void setConner_l(Point conner_l) {
        this.conner_l = conner_l;
    }

    public void setConner_r(Point conner_r) {
        this.conner_r = conner_r;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUploadManagerService(UploadManagerService uploadManagerService) {
        this.uploadManagerService = uploadManagerService;
    }

    public void setDownloadManagerService(DownloadManagerService downloadManagerService) {
        this.downloadManagerService = downloadManagerService;
    }

    public void setNotificationBarService(NotificationBarService notificationBarService) {
        this.notificationBarService = notificationBarService;
    }


    public String getToken() {
        return token;
    }

    public long getLimitData() {
        return limitData;
    }

    public Context getContext() {
        return context;
    }

    public Point getConner_l() {
        return conner_l;
    }

    public Point getConner_r() {
        return conner_r;
    }

    public UploadManagerService getUploadManagerService() {
        return uploadManagerService;
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
    public void uploadStart(){uploadNow =true;}
    public void uploadFinish(){uploadNow=false;}

    public boolean isUploadNow() {
        return uploadNow;
    }
    public boolean isAbleDownload(){
        return downloadNow;
    }

}
