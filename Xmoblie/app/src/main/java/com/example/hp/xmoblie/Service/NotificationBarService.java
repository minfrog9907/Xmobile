package com.example.hp.xmoblie.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Utill.NotificationHandler;

/**
 * Created by HP on 2017-11-02.
 */

public class NotificationBarService extends Service {
    String id = "my_channel_01";
    CharSequence name = "Xmobile";
    String description = "Xmobile";
    Notification notification;
    NotificationManager notifi_M;
    IBinder mBinder = new LocalBinder();
    NotificationHandler handler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new NotificationHandler();

        notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(id, name, importance);

            mChannel.setDescription(description);
            mChannel.enableLights(true);

            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(false);


            notifi_M.createNotificationChannel(mChannel);



            Log.e("Created", "Nofitication Created O");

        } else {

            Log.e("Created", "Nofitication Created");
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public NotificationHandler addService() {
        return handler;
    }

    public void makeNotification(String title,String content){
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("my_channel_01")
                    .build();
        }
        else{
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
        }
    }
    public void makeNotification(String title,String content,int process,int max,boolean loading){
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(getApplicationContext())
                    .setProgress(max,process,loading)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("my_channel_01")
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;

        }
        else{
            notification = new Notification.Builder(getApplicationContext())
                    .setProgress(max,process,true)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;

        }
    }

    public void deleteNotification(int id){
        notifi_M.cancel(id);
    }
    public void pushNotification(int id){
        notifi_M.notify(id, notification);

    }
    public class LocalBinder extends Binder {
        public NotificationBarService getServerInstance() {
            return NotificationBarService.this;
        }
    }

}
