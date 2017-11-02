package com.example.hp.xmoblie.Utill;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.Service.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-30.
 */

public class DownloadManager extends Service {
    ApiClient apiClient;
    String filename = "";
    String path = "";
    String token = "";
    long offet = 4096;
    int length = 0;
    int left;
    int type = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        filename = intent.getStringExtra("filename");
        path = intent.getStringExtra("path");
        token = intent.getStringExtra("token");
        offet = intent.getLongExtra("offset", 0);
        length = intent.getIntExtra("length", 4096);
        type = intent.getIntExtra("type", 1);
        left = length;

        try {
            downloadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void downloadFile() throws IOException {
        DownLoadMotherThread dlm = new DownLoadMotherThread();
        dlm.run(type, filename, path, token, offet, length);
    }



}
