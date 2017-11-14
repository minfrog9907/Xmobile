package com.example.hp.xmoblie.Service;

import android.util.Log;

import com.example.hp.xmoblie.Items.DownloadRequestItem;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.LogItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-10.
 */

public class FilemanagerService {
    private static FilemanagerService instance;
    public static FilemanagerService getInstance() {
        if (instance == null)
            return instance = new FilemanagerService();
        else
            return instance;
    }
    ArrayList<FileItem> fileItemList;
    private ApiClient apiClient = ApiClient.service;

    /* 파일 다운로드 */
    public void downloadFileStart(ArrayList<FileItem> fileItemList){
        this.fileItemList = fileItemList;
        downloadFile(fileItemList.get(0));
    }

    private void downloadFile(FileItem fileItem){
        String fileName = fileItem.getFilename();
        int fileSize = (int)fileItem.getSize();
        try {
            ServiceControlCenter
                    .getInstance()
                    .getDownloadManagerService()
                    .downloadFile(new DownloadRequestItem(1, fileName, "", 0, fileSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFinish(){
        if( fileItemList.contains(fileItemList.get(0)) ){
            fileItemList.remove(fileItemList.get(0));
            if(fileItemList.size() > 0){
                downloadFile(fileItemList.get(0));
            }
        }
    }

    /* 파일 공유 */
    public void shareFileStart(FileItem fileItem) {
        Log.d("clicked button", "share");
    }

    private void shareFile(FileItem fileItem){

    }

    /* 파일 로그 */
    public void fileLogStart(ArrayList<FileItem> fileItem, String path, String token) {
        Log.d("clicked button", "filelog");
        for(int i = 0; i < fileItem.size(); i++){
            String filename = fileItem.get(0).getFilename();
            fileLog(path, filename, token);
            System.out.println("나 돌아유");
        }
    }

    private void fileLog(String path, String filename, String token){
        final Call<List<LogItem>> call = apiClient.repoFileLog(token,path,filename);
        call.enqueue(new Callback<List<LogItem>>() {
            @Override
            public void onResponse(Call<List<LogItem>> call,
                                   Response<List<LogItem>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); ++i) {
                        System.out.println(response.body().get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LogItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
            }
        });
    }

    /* 파일 정보 */
    public void fileInfoStart() {
        Log.d("clicked button", "fileinfo");
    }
}
