package com.example.hp.xmoblie.Thread;

import android.util.Log;

import com.example.hp.xmoblie.Service.ApiClient;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-20.
 */

public class UploadThread extends Thread{
    private RequestBody requestBody;
    private int id,recallTime=0;
    private UploadMotherThread uploadMotherThread;
    private ApiClient apiClient = ApiClient.severService;
    public void run(){
        startUpload();
    }

    public void setData(RequestBody requestBody,int id, UploadMotherThread umt){
        this.requestBody =requestBody;
        this.id =id;
        this.uploadMotherThread=umt;
    }
    @Override
    public void interrupt() {
        super.interrupt();
        Log.e("kill","kill UT");
    }
    private void startUpload(){
        Call<ResponseBody> call = apiClient.repoUpload(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Upload", "쓰레드 성공");
                try {
                    uploadMotherThread.reportDead(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload",t.getMessage());
                if(recallTime++!=4){
                    uploadMotherThread.recall(id);
                }
                if (recallTime==4){
                    uploadMotherThread.badRepoDie(id);
                }
            }
        });
    }
}
