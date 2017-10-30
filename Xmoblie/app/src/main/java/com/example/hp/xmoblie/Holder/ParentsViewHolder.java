package com.example.hp.xmoblie.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by HP on 2017-10-27.
 */

public class ParentsViewHolder extends GroupViewHolder {

    private TextView fileName;
    private ImageView fileIcon;

    public ParentsViewHolder(View itemView) {
        super(itemView);
        fileName = (TextView) itemView.findViewById(R.id.fileName);
        fileIcon = (ImageView) itemView.findViewById(R.id.fileIcon);
    }

    public void setParentsFileitem(ExpandableGroup group){
        
    }
}
