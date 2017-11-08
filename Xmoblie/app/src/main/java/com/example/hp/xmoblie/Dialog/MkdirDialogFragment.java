package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-03.
 */

public class MkdirDialogFragment extends CreateDialogFragment{
    private static final String ARG_DIALOG_SEARCHDATA = "dialog_main_searchdata";
    private EditText etnewDir;
    private String searchData;
    private InputListener listener;


    public static MkdirDialogFragment newInstance(InputListener listener, String searchData) {
        Bundle bundle = new Bundle();
        MkdirDialogFragment  fragment = new MkdirDialogFragment ();

        fragment.listener = listener;
        bundle.putString(ARG_DIALOG_SEARCHDATA, searchData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchData = getArguments().getString(ARG_DIALOG_SEARCHDATA);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_mkdir, null);
        etnewDir = (EditText) view.findViewById(R.id.editdata_edit);

        ((TextView) view.findViewById(R.id.tvTarget)).setText("'" + searchData + "' 경로에 새 폴더가 생성됩니다.");
        view.findViewById(R.id.editdata_cancel).setOnClickListener(this);
        view.findViewById(R.id.editdata_commit).setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editdata_cancel:
                dismissDialog();
                break;
            case R.id.editdata_commit:
                if(etnewDir.getText() != null) {
                    if (listener.onInputComplete(etnewDir.getText().toString())) {
                        dismissDialog();
                    }
                }
                break;
        }
    }
}
