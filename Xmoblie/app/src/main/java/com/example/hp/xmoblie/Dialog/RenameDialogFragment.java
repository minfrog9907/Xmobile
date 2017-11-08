package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-07.
 */

public class RenameDialogFragment extends CreateDialogFragment{
    private static final String ARG_DIALOG_RENAMEFILE = "dialog_main_renamefile";
    private EditText etRenameFile;
    private String oldName;
    private InputListener listener;


    public static RenameDialogFragment newInstance(InputListener listener, String oldName) {
        Bundle bundle = new Bundle();
        RenameDialogFragment fragment = new RenameDialogFragment ();

        fragment.listener = listener;
        bundle.putString(ARG_DIALOG_RENAMEFILE, oldName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldName = getArguments().getString(ARG_DIALOG_RENAMEFILE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_mkdir, null);
        etRenameFile = (EditText) view.findViewById(R.id.editdata_edit);
        etRenameFile.setText(oldName);

        ((TextView) view.findViewById(R.id.tvTarget)).setText("'" + oldName + "' 의 이름을 변경합니다..");
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
                if (listener.onInputComplete(etRenameFile.getText().toString())) {
                    dismissDialog();
                }
                break;
        }
    }
}