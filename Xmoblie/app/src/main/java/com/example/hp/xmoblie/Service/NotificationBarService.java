package com.example.hp.xmoblie.Service;

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
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.MainActivity;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Utill.DownloadMotherThread;
import com.example.hp.xmoblie.Utill.NotificationHandler;

/**
 * Created by HP on 2017-11-02.
 */

public class NotificationBarService extends Service {
    NotificationManager Notifi_M;
    IBinder mBinder = new LocalBinder();
    NotificationHandler  handler;
    Notification.Builder builder;
    int max;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Notifi_M = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new NotificationHandler();
        Log.e("Created","Nofitication Created");
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
        Log.e("asd","asd");
        Intent intent = new Intent(NotificationBarService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationBarService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_launcher).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle("다운로드중").setContentText("다운로드중")
                .setProgress(100,0,true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        Notifi_M.notify( 1 , builder.build());
    }
    public void setDownLoadMax(int max){
        this.max =max;
        builder.setProgress(max,0,true);
        Notifi_M.notify( 1 , builder.build());
    }

    public void updateDownload(int process){
        builder.setProgress(max,process,true);
        Notifi_M.notify( 1 , builder.build());
    }
    public NotificationHandler addService(){
       return handler;
    }


    public class LocalBinder extends Binder {
        public NotificationBarService getServerInstance() {
            return NotificationBarService.this;
        }
    }

}
