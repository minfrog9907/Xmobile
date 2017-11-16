package com.example.hp.xmoblie.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-11-15.
 */

public class AddTagDialogFragment extends CreateDialogFragment{
    private static final String ARG_DIALOG_RENAMEFILE = "dialog_main_renamefile";
    private EditText etTagFile;
    private String fileName;
    private InputListener listener;


    public static AddTagDialogFragment newInstance(InputListener listener, String fileName) {
        Bundle bundle = new Bundle();
        AddTagDialogFragment fragment = new AddTagDialogFragment ();

        fragment.listener = listener;
        bundle.putString(ARG_DIALOG_RENAMEFILE, fileName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getArguments().getString(ARG_DIALOG_RENAMEFILE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_addtag, null);
        etTagFile = (EditText) view.findViewById(R.id.editdata_edit);

        ((TextView) view.findViewById(R.id.tvTarget)).setText("'" + fileName + "' 에 태그를 추가합니다.");
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
                if (listener.onInputComplete(etTagFile.getText().toString())) {
                    dismissDialog();
                }
                break;
        }
    }
}
