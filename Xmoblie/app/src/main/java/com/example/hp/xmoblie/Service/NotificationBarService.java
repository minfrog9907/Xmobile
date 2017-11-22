package com.example.hp.xmoblie.Service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.FileManagerActivity;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Utill.NotificationHandler;

import java.io.File;
import java.util.List;

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

    public void makeNotification(String title, String content) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("my_channel_01")
                    .build();
        } else {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
        }
    }

    public void makeNotification(String title, String content, String filename, String path) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            Intent intent = viewFile(filename);
            PendingIntent pending = PendingIntent.getActivity(ServiceControlCenter.getInstance().getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pending)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("my_channel_01")
                    .build();
        } else {
            Intent intent = viewFile(filename);
            PendingIntent pending = PendingIntent.getActivity(ServiceControlCenter.getInstance().getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pending)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
        }
    }

    public void makeNotification(String title, String content, int process, int max, boolean loading) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(getApplicationContext())
                    .setProgress(max, process, loading)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("my_channel_01")
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;

        } else {
            notification = new Notification.Builder(getApplicationContext())
                    .setProgress(max, process, true)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;

        }
    }

    public void deleteNotification(int id) {
        notifi_M.cancel(id);
    }

    public void pushNotification(int id) {
        notifi_M.notify(id, notification);

    }


    private Intent viewFile(String fileName) {

        Context ctx = this;
        Intent fileLinkIntent = new Intent(Intent.ACTION_VIEW);// 뷰형태
        fileLinkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(new File(Environment.getExternalStorageDirectory(),"XMobileDownLoad"),fileName); // 파일 불러옴 여기가 널이면 처리하면될듯  if (file.exists()) 파일유무 확인

        fileLinkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileLinkIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Uri uri = FileProvider.getUriForFile(this,"com.example.hp.xmoblie.provider", file);
        //확장자 구하기
        String fileExtend = getExtension(file.getAbsolutePath());
        // 파일 확장자 별로 mime type 지정해 준다.
        if (fileExtend.equalsIgnoreCase("mp3")) {
            fileLinkIntent.setDataAndType(uri, "audio/*");
        } else if (fileExtend.equalsIgnoreCase("mp4")) {
            fileLinkIntent.setDataAndType(uri, "video/*");
        } else if (fileExtend.equalsIgnoreCase("jpg")
                || fileExtend.equalsIgnoreCase("jpeg")
                || fileExtend.equalsIgnoreCase("gif")
                || fileExtend.equalsIgnoreCase("png")
                || fileExtend.equalsIgnoreCase("bmp")) {
            fileLinkIntent.setDataAndType(uri, "image/*");
        } else if (fileExtend.equalsIgnoreCase("txt")) {
            fileLinkIntent.setDataAndType(uri, "text/*");
        } else if (fileExtend.equalsIgnoreCase("doc")
                || fileExtend.equalsIgnoreCase("docx")) {
            fileLinkIntent.setDataAndType(uri, "application/msword");
        } else if (fileExtend.equalsIgnoreCase("xls")
                || fileExtend.equalsIgnoreCase("xlsx")) {
            fileLinkIntent.setDataAndType(uri,
                    "application/vnd.ms-excel");
        } else if (fileExtend.equalsIgnoreCase("ppt")
                || fileExtend.equalsIgnoreCase("pptx")) {
            fileLinkIntent.setDataAndType(uri,
                    "application/vnd.ms-powerpoint");
        } else if (fileExtend.equalsIgnoreCase("pdf")) {
            fileLinkIntent.setDataAndType(uri, "application/pdf");
        } else if (fileExtend.equalsIgnoreCase("hwp")) {
            fileLinkIntent.setDataAndType(uri,
                    "application/haansofthwp");
        }
        PackageManager pm = ctx.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(fileLinkIntent,
                PackageManager.GET_META_DATA);
        if (list.size() == 0) {
            Toast.makeText(ctx, fileName + "을 확인할 수 있는 앱이 설치되지 않았습니다.",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return fileLinkIntent;
        }
    }

    private String getExtension(String fileStr) {
        return fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
    }

    public class LocalBinder extends Binder {
        public NotificationBarService getServerInstance() {
            return NotificationBarService.this;
        }
    }

}
