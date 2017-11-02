package com.example.hp.xmoblie.Utill;

/**
 * Created by HP on 2017-11-02.
 */

public class ServiceControlCenter {
    private static ServiceControlCenter instance;

    DownloadManager downloadManager;
    NotificationBarService notificationBarService;

    public static ServiceControlCenter getInstance() {
        if (instance == null)
            return instance = new ServiceControlCenter();
        else
            return instance;
    }

    public void setDownloadManager(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public void setNotificationBarService(NotificationBarService notificationBarService) {
        this.notificationBarService = notificationBarService;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public NotificationBarService getNotificationBarService() {
        return notificationBarService;
    }

    public void downloadFinish(){
        downloadManager = null;
    }

    public boolean isAbleDownload(){
        if(downloadManager == null)
            return true;
        else
            return false;
    }

}
