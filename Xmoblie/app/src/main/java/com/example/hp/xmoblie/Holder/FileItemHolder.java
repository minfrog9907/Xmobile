package com.example.hp.xmoblie.Holder;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.Items.FileItem;

/**
 * Created by HP on 2017-10-30.
 */

public class FileItemHolder {
    public TextView fileName;
    public ImageView fileIcon;
    public ImageView showMoreMenu;
    public CheckBox checkBox;
    public FileItem realFileItem;
}
//
//                        .putExtra("type",1)
//                                .putExtra("filename","winserver.png")
//                                .putExtra("path","\\")
//                                .putExtra("token",getIntent().getStringExtra("token"))
//                                .putExtra("offset",0)
//                                .putExtra("length", 54085)