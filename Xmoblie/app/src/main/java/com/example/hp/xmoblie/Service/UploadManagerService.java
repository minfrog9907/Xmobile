package com.example.hp.xmoblie.Service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Thread.UploadMotherThread;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-07.
 */

public class UploadManagerService extends Service {
    ApiClient apiClient;
    IBinder mBinder = new UploadManagerService.LocalBinder();
    UploadMotherThread uploadMotherThread;
    String token;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiClient = ApiClient.service;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        token = ServiceControlCenter.getInstance().getToken();
        //uploadFile(intent.getStringExtra("path"),intent.getStringExtra("filename"),intent.getStringExtra("target"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void uploadFile(String path, String filename, String target,long size) throws IOException {
        Log.e("offset len",size+"");

        if (!ServiceControlCenter.getInstance().isUploadNow()) {
            if (size > 20971520) {
                Log.e("upload","start");
                uploadMotherThread = new UploadMotherThread();
                uploadMotherThread.run(filename,target,path,size);
                ServiceControlCenter.getInstance().uploadStart();
            } else if (size <= 20971520) {
                ServiceControlCenter.getInstance().uploadStart();
                uploadFile_under20MB(path, filename, target);
            }
        }
    }

    private void uploadFile_under20MB(String path, String filename, String target) {
        // create upload service client

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        Log.e("path", (path) + "/"+filename);
        File file = new File(path + "/" + filename);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(path + "/" + filename),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "file data";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = apiClient.repoUpload(token, description, body, target);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void shareURL(String path) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", "https://xmobile.lfconfig.xyz/share?path=" + path);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "공유링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
    }
    public void dead() {
        if (uploadMotherThread != null && uploadMotherThread.isAlive()) {
            uploadMotherThread.interrupt();
            Log.e("kill","kill UT");
        }
        ServiceControlCenter.getInstance().uploadFinish();
    }

    public void cancelUpload(String filename){
        if(uploadMotherThread.getFilename().equals(filename))
            uploadMotherThread.freeForChild();
    }

    public class LocalBinder extends Binder {
        public UploadManagerService getServerInstance() {
            return UploadManagerService.this;

        }
    }
}
