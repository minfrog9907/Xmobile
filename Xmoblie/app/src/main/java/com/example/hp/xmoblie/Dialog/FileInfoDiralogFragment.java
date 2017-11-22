package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.xmoblie.Adapter.FileTagListAdapter;
import com.example.hp.xmoblie.Adapter.LogListAdapter;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-17.
 */

public class FileInfoDiralogFragment extends CreateDialogFragment {
    private Context context;
    private FileItem fileItem;
    private TextView tvFileName, tvFileSize, tvFileUploader, tvCID, tvGID, tvUploadTime, tvLastUpdate;
    private ListView lvTags;
    private ApiClient apiClient;
    private String token;

    public static FileInfoDiralogFragment newInstance(FileItem fileItem, Context context) {

        FileInfoDiralogFragment fragment = new FileInfoDiralogFragment();
        fragment.fileItem = fileItem;
        fragment.context = context;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fileinfo, null);
        tvFileName = view.findViewById(R.id.fileInfo_fileName);
        tvFileSize = view.findViewById(R.id.fileInfo_fileSize);
        tvFileUploader = view.findViewById(R.id.fileInfo_fileUploader);
        tvCID = view.findViewById(R.id.fileInfo_fileCID);
        tvGID = view.findViewById(R.id.fileInfo_fileGID);
        tvUploadTime = view.findViewById(R.id.fileInfo_fileUploadTime);
        tvLastUpdate = view.findViewById(R.id.fileInfo_fileLastUpdate);
        lvTags = view.findViewById(R.id.tagList);
        apiClient = ApiClient.service;

        String fileName = fileItem.getFilename() != null ? fileItem.getFilename() : "";
        long fileSize = fileItem.getSize() != 0 ? fileItem.getSize() : 0;
        String fileUploader = fileItem.getUserid() != null ? fileItem.getUserid() : "";
        int fileCID = fileItem.getGgid() != 0 ? fileItem.getGgid() : 0;
        int fileGID = fileItem.getGid() != 1 ? fileItem.getGid() : 1;
        String fileUploadTime = fileItem.getParseCreateDate() != null ? fileItem.getParseCreateDate() : "";
        String fileLastUpdate = fileItem.getParseLastWriteDate() != null ? fileItem.getParseLastWriteDate() : "";

        tvFileName.setText(fileName);
        tvFileSize.setText(FileUtils.byteCountToDisplaySize(fileSize));
        tvFileUploader.setText(fileUploader);
        tvCID.setText(Integer.toString(fileItem.getGgid()) );
        tvGID.setText(Integer.toString(fileItem.getGid()) );
        tvUploadTime.setText(fileUploadTime);
        tvLastUpdate.setText(fileLastUpdate);

        if(fileItem.getTags() != null) {
            createTagList(fileItem.getTags());
        }
        view.findViewById(R.id.editdata_commit).setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    private void createTagList(ArrayList<String> tags){
        FileTagListAdapter fileTagListAdapter = new FileTagListAdapter(context, tags);
        lvTags.setAdapter(fileTagListAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editdata_commit:
                dismissDialog();
                break;
        }
    }
}