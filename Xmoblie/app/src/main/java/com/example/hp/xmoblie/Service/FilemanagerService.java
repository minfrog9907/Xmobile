package com.example.hp.xmoblie.Service;

import android.util.Log;

import com.example.hp.xmoblie.Items.DownloadRequestItem;
import com.example.hp.xmoblie.Items.FileItem;

import com.example.hp.xmoblie.Items.RollbackItem;


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
    private List<RollbackItem> logItems = new ArrayList<RollbackItem>();
    private static FilemanagerService instance;
    private int cnt = 0;

    public static FilemanagerService getInstance() {
        if (instance == null)
            return instance = new FilemanagerService();
        else
            return instance;
    }

    ArrayList<FileItem> fileItemList;
    private ApiClient apiClient = ApiClient.service;


    /* 파일 다운로드 */
    public void downloadFileStart(ArrayList<FileItem> fileItemList) {
        cnt = 0;
        this.fileItemList = fileItemList;
        System.out.println(fileItemList);
        try {
            downloadFile(fileItemList.get(cnt));
        } catch (IndexOutOfBoundsException e) {
        }
    }

    private void downloadFile(FileItem fileItem) {
        String fileName = fileItem.getFilename();
        if (fileItem.getType() == 128) {
            System.out.println("나 다운로드 해오 : " + fileName);
            int fileSize = (int) fileItem.getSize();
            try {
                ServiceControlCenter
                        .getInstance()
                        .getDownloadManagerService()
                        .downloadFile(new DownloadRequestItem(1, fileName, "", 0, fileSize));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadFinish() {
        cnt++;
        try {
            downloadFile(fileItemList.get(cnt));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("다운로드 끝");
        }
    }

    /* 파일 공유 */
    public void shareFileStart() {
        Log.d("clicked button", "share");
    }

    private void shareFile(FileItem fileItem) {

    }

    /* 파일 정보 */
    public void fileInfoStart() {
        Log.d("clicked button", "fileinfo");
    }
}
