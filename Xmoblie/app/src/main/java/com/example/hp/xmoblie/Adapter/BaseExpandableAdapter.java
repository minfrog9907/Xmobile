package com.example.hp.xmoblie.Adapter;

/**
 * Created by HP on 2017-10-16.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.Animation.AnimatedExpandableListView;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ium on 14. 2. 26.
 */
public class BaseExpandableAdapter extends AnimatedExpandableListAdapter {

    private Context _context;
    private List<FileItem> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<FileItem, List<FileItem>> _listDataChild;

    public BaseExpandableAdapter(Context context, List<FileItem> listDataHeader,
                                 HashMap<FileItem, List<FileItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public FileItem getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition).getFilename();
        int type = (int) getChild(groupPosition, childPosition).getType();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.file_list_row, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.childFileName);
        LinearLayout childLine = (LinearLayout) convertView.findViewById(R.id.childLine);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.childFileIcon);
        LinearLayout showMoreMenu = (LinearLayout) convertView.findViewById(R.id.showMoreMenu);

        if(type == 128){
            showMoreMenu.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.file);
            imageView.setTag("file");
        }else{
            imageView.setImageResource(R.drawable.folder);
            imageView.setTag("folder");
        }

        imageView.setImageResource(R.drawable.file);
        childLine.setVisibility(View.VISIBLE);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public FileItem getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition).getFilename();
        int type = (int) getGroup(groupPosition).getType();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.file_list_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.fileName);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.fileIcon);
        LinearLayout showMoreMenu = (LinearLayout) convertView.findViewById(R.id.showMoreMenu);
        if(type == 128){
            showMoreMenu.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.file);
            imageView.setTag("file");
        }else{
            imageView.setImageResource(R.drawable.folder);
            imageView.setTag("folder");
        }
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
