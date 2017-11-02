package com.example.hp.xmoblie.Utill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-02.
 */

public class NotificationBarService extends Service {
    NotificationManager Notifi_M;
    Notification Notifi ;
    IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void startDownload(){
        Notifi = new Notification.Builder(getApplicationContext())
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("알림!!!")
                .build();



        //확인하면 자동으로 알림이 제거 되도록
        Notifi.flags = Notification.FLAG_AUTO_CANCEL;


        Notifi_M.notify( 777 , Notifi);
    }

    public class LocalBinder extends Binder {
        public NotificationBarService getServerInstance() {
            return NotificationBarService.this;
        }
    }

}
