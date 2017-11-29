package com.example.hp.xmoblie.Service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.FileManagerActivity;
import com.example.hp.xmoblie.Dialog.CreateDialogFragment;
import com.example.hp.xmoblie.Dialog.FileInfoDiralogFragment;
import com.example.hp.xmoblie.Items.DownloadRequestItem;
import com.example.hp.xmoblie.Items.FileItem;

import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.R;


import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.lang.reflect.Array;
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
    private Context fcontext;
    private FragmentManager fragmentManager;
    private String path;

    public static FilemanagerService getInstance() {
        if (instance == null)
            return instance = new FilemanagerService();
        else
            return instance;
    }

    ArrayList<FileItem> fileItemList;
    private ApiClient apiClient = ApiClient.service;


    /* 파일 다운로드 */
    public void downloadFileStart(ArrayList<FileItem> fileItemList, Context context, String path) {
        cnt = 0;
        this.fileItemList = fileItemList;
        this.fcontext = context;
        this.path = path;
        try {
            ckdownloadFile(fileItemList.get(cnt));
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void downloadFileStart(FileItem ffileItem, Context context) {
        cnt = 0;
        this.fcontext = context;
        try {
            ckdownloadFile(ffileItem);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    private Dialog confirmDialog(String title, String fileName, int fileSize) {
        final String filename = fileName;
        final int filesize = fileSize;
        AlertDialog.Builder builder = new AlertDialog.Builder(fcontext);
        builder.setMessage(title)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(fcontext, "확인됨", Toast.LENGTH_SHORT).show();
                        downloadFile(filename, filesize);
                    }
                })
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(fcontext, "취소됨", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

    private void ckdownloadFile(FileItem fileItem) {
        String fileName = fileItem.getFilename();
        if (fileItem.getType() == 128) {
            int fileSize = (int) fileItem.getSize();
            if(ServiceControlCenter.getInstance().getLimitData() != 0 && fileSize >= ServiceControlCenter.getInstance().getLimitData()){
                confirmDialog("제한 파일 크기를 초과하는 파일 입니다.\n" +
                                "다운로드 하시겠습니까?\n\n" +
                                "파일 크기 : " + FileUtils.byteCountToDisplaySize(fileSize) ,
                        fileName,
                        fileSize).show();
            }else{
                downloadFile(fileName, fileSize);
            }
        }
    }

    private void downloadFile(String fileName, int fileSize){
        System.out.println("나 다운로드 해오 : " + fileName);
        try {
            ServiceControlCenter
                    .getInstance()
                    .getDownloadManagerService()
                    .downloadFile(new DownloadRequestItem(3, fileName, path, 0, fileSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFinish() {
        cnt++;
        try {
            ckdownloadFile(fileItemList.get(cnt));
        } catch (Exception e) {
            System.out.println("다운로드 끝");
        }
    }

    /* 파일 공유 */
    public void shareFile(String path) {
        String rpath = path.replace("\\", "\\\\");
        UploadManagerService uploadManagerService = new UploadManagerService();
        uploadManagerService.shareURL(rpath);
    }

    /* 파일 정보 */
    public void fileInfoStart(String token, String path, FileItem fileItem, Context context, FragmentManager manager) {
        String fileName = fileItem.getFilename();
        int fileType = fileItem.getType();
        fileInfoProtocol(token,path,fileName,fileType, context, manager);
    }

    private void fileInfoProtocol(final String token, final String path, final String fileName, int fileType, final Context context, final FragmentManager manager) {
        final Call<FileItem> call = apiClient.repoFileInfo(token, path, fileName, fileType);
        call.enqueue(new Callback<FileItem>() {
            @Override
            public void onResponse(Call<FileItem> call,
                                   Response<FileItem> response) {
                if (response.body() != null) {
                    FileItem fileItem = (FileItem) response.body();
                    fcontext = context;
                    fragmentManager = manager;
                    fileTagsProtocol(token, path,fileName, fileItem);
                }
            }

            @Override
            public void onFailure(Call<FileItem> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
            }
        });
    }

    private void fileTagsProtocol(String token, String path, String fileName, final FileItem fileItem){
        final ArrayList<String> tags = new ArrayList<String>();
        final Call<ArrayList<String>> call = apiClient.repoFileTags(token, path, fileName);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call,
                                   Response<ArrayList<String>> response) {
                if (response.body() != null) {
                    for(int i = 0; i < response.body().size(); i++){
                        tags.add(response.body().get(i));
                        fileItem.setTags(tags);
                    }
                    CreateDialogFragment dialog = FileInfoDiralogFragment.newInstance(fileItem,fcontext);
                    dialog.show(fragmentManager, "fileInfo");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
            }
        });
    }


}