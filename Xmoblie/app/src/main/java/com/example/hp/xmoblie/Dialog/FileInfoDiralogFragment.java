package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.FileManagerActivity;
import com.example.hp.xmoblie.Adapter.FileTagListAdapter;
import com.example.hp.xmoblie.Adapter.LogListAdapter;
import com.example.hp.xmoblie.Holder.TagItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.Items.TagItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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
    private String rtoken, rpath;

    public static FileInfoDiralogFragment newInstance(FileItem fileItem, Context context, String token, String path) {

        FileInfoDiralogFragment fragment = new FileInfoDiralogFragment();
        fragment.fileItem = fileItem;
        fragment.context = context;
        fragment.rtoken = token;
        fragment.rpath = path;

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

        String displayfileName = fileItem.getDisplayName() != null ? fileItem.getDisplayName() : "";
        long fileSize = fileItem.getSize() != 0 ? fileItem.getSize() : 0;
        String fileUploader = fileItem.getUserid() != null ? fileItem.getUserid() : "";
        int fileCID = fileItem.getGgid() != 0 ? fileItem.getGgid() : 0;
        int fileGID = fileItem.getGid() != 1 ? fileItem.getGid() : 1;
        String fileUploadTime = fileItem.getParseCreateDate() != null ? fileItem.getParseCreateDate() : "";
        String fileLastUpdate = fileItem.getParseLastWriteDate() != null ? fileItem.getParseLastWriteDate() : "";

        tvFileName.setText(displayfileName);
        tvFileSize.setText(FileUtils.byteCountToDisplaySize(fileSize));
        tvFileUploader.setText(fileUploader);
        tvCID.setText(Integer.toString(fileCID) );
        tvGID.setText(Integer.toString(fileGID) );
        tvUploadTime.setText(fileUploadTime);
        tvLastUpdate.setText(fileLastUpdate);

        if(fileItem.getTags() != null) {
            createTagList(fileItem.getTags());
        }
        view.findViewById(R.id.editdata_commit).setOnClickListener(this);
        builder.setView(view);

        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = (String)view.getTag();
                String filename = fileItem.getFilename();
                String displayf = fileItem.getDisplayName();
                String token = rtoken;
                String path = rpath;
                dialogDel(filename, displayf, tag, token, path).show();
            }
        });

        return builder.create();
    }

    private void createTagList(ArrayList<String> tags){
        FileTagListAdapter fileTagListAdapter = new FileTagListAdapter(context, tags);
        lvTags.setAdapter(fileTagListAdapter);
    }

    private void deleteTag(String tag, String filename, String path, String token){
        final Call<ResponseBody> call = apiClient.repoDelTag(token,tag,filename,path);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.body() != null) {
                    Toast.makeText(context, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
            }
        });
    }

    private Dialog dialogDel(final String filename,final String displayf,final String tag,final String token,final String path){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(displayf + " 에서 " + tag + "태그를 삭제하시겠습니까?")
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTag(tag, filename, path, token);
                        dialogInterface.cancel();
                        dismissDialog();
                    }
                })
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
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