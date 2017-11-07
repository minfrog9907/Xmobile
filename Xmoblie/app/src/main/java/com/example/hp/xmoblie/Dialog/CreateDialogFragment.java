package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.xmoblie.Activity.FileManagerActivity;
import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-03.
 */

public class CreateDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String ARG_DIALOG_SEARCHDATA = "dialog_main_searchdata";
    private ApiClient apiClient;
    private EditText etnewDir;
    private String searchData;
    private DirInputListener listener;

    public static CreateDialogFragment newInstance(DirInputListener listener, String searchData) {
        Bundle bundle = new Bundle();
        CreateDialogFragment fragment = new CreateDialogFragment();

        fragment.listener = listener;
        bundle.putString(ARG_DIALOG_SEARCHDATA, searchData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface DirInputListener {
        boolean onDirInputComplete(String name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = ApiClient.service;
        searchData = getArguments().getString(ARG_DIALOG_SEARCHDATA);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_mkdir, null);
        etnewDir = (EditText) view.findViewById(R.id.editdata_edit);

        ((TextView) view.findViewById(R.id.targetDir)).setText("'" + searchData + "' 경로에 새 폴더가 생성됩니다.");
        view.findViewById(R.id.editdata_cancel).setOnClickListener(this);
        view.findViewById(R.id.editdata_commit).setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    private void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.editdata_cancel:
                dismissDialog();
                break;
            case R.id.editdata_commit:
                if (listener.onDirInputComplete(etnewDir.getText().toString())) {
                    dismissDialog();
                }
                break;
        }
    }
}
