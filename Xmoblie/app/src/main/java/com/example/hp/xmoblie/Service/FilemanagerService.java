package com.example.hp.xmoblie.Service;

import android.util.Log;

import com.example.hp.xmoblie.Activity.FileManagerActivity;
import com.example.hp.xmoblie.Items.DownloadRequestItem;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Utill.ServiceControlCenter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by HP on 2017-11-10.
 */

public class FilemanagerService {

    ArrayList<FileItem> fileItemList;

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
                    .downloadFile(new DownloadRequestItem(1, fileName, "", 0, fileSize),new FilemanagerService());
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
    public void fileLogStart(ArrayList<FileItem> fileItem) {
        Log.d("clicked button", "filelog");
        for(int i = 0; i < fileItem.size(); i++){
            System.out.println(fileItem.get(i));
        }
    }

    /* 파일 정보 */
    public void fileInfoStart() {
        Log.d("clicked button", "fileinfo");
    }
}
