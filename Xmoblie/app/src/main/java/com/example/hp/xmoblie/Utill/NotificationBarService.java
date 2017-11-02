package com.example.hp.xmoblie.Utill;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;

import com.example.hp.xmoblie.Activity.MainActivity;
import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-02.
 */

public class NotificationBarService extends Service {
    NotificationManager Notifi_M;
    IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notifi_M = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void startDownload(){
        Intent intent = new Intent(NotificationBarService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationBarService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_launcher).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        Notifi_M.notify( 777 , builder.build());
    }


    public class LocalBinder extends Binder {
        public NotificationBarService getServerInstance() {
            return NotificationBarService.this;
        }
    }

}
