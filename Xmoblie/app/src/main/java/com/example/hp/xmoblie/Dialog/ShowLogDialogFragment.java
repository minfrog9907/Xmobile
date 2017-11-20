package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.xmoblie.Adapter.LogListAdapter;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-13.
 */

public class ShowLogDialogFragment extends CreateDialogFragment {
    private static final String ARG_DIALOG_RENAMEFILE = "dialog_main_renamefile";
    private List<RollbackItem> logItems = new ArrayList<RollbackItem>();
    private ListView listView;
    private TextView tvTarget;
    private ApiClient apiClient = ApiClient.service;
    private Context context;

    private ArrayList<FileItem> fileItems;
    private String path;
    private String token;

    public static ShowLogDialogFragment newInstance(ArrayList<FileItem> fileItems, String path, String token, Context context) {
//        Bundle bundle = new Bundle();
//        bundle.putString(ARG_DIALOG_RENAMEFILE, filename);
//        fragment.setArguments(bundle);

        ShowLogDialogFragment fragment = new ShowLogDialogFragment();
        fragment.fileItems = fileItems;
        fragment.path = path;
        fragment.token = token;
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_showlog, null);
        listView = (ListView) view.findViewById(R.id.logList);
        tvTarget = (TextView) view.findViewById(R.id.tvTarget);
        tvTarget.setText("'"+fileItems.get(0).getFilename() + "' 로그 목록");
        fileLogStart(fileItems, path, token);

        view.findViewById(R.id.editdata_commit).setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    private void fileLog(String path, String filename, String token) {

        final Call<List<RollbackItem>> call = apiClient.repoFileLog(token, path, filename);
        call.enqueue(new Callback<List<RollbackItem>>() {
            @Override
            public void onResponse(Call<List<RollbackItem>> call,
                                   Response<List<RollbackItem>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); ++i) {
                        logItems.add(response.body().get(i));
                    }
                    LogListAdapter logListAdapter = new LogListAdapter(context,logItems);
                    listView.setAdapter(logListAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<RollbackItem>> call, Throwable t) {
                Log.e("jsonResponse", "빼애애앵ㄱ");
            }
        });
    }

    /* 파일 로그 */
    public void fileLogStart(ArrayList<FileItem> fileItem, String path, String token) {
        String filename = fileItem.get(0).getFilename();
        fileLog(path, filename, token);
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