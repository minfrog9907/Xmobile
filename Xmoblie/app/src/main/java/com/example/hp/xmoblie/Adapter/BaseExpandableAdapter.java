package com.example.hp.xmoblie.Adapter;

/**
 * Created by HP on 2017-10-16.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.Animation.AnimatedExpandableListView;
import com.example.hp.xmoblie.Animation.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.example.hp.xmoblie.Holder.ParentsViewHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;
import com.yalantis.ucrop.view.OverlayView;

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
    private RecyclerView.ViewHolder viewHolder = null;

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
        ImageView showMoreMenu = (ImageView) convertView.findViewById(R.id.showMoreMenu);

        if (type == 128) {
            imageView.setImageResource(R.drawable.file);
            imageView.setTag("file");
        } else {
            imageView.setImageResource(R.drawable.folder);
            imageView.setTag("folder");
        }
        showMoreMenu.setVisibility(View.INVISIBLE);
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
        ImageView showMoreMenu = (ImageView) convertView.findViewById(R.id.showMoreMenu);
        if (type == 128) {
            convertView.setTag("file");
            showMoreMenu.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.file);
            imageView.setTag("file");
        } else {
            convertView.setTag("folder");
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

    public void refresh(){
        notifyDataSetChanged();
    }

    public void changeSelectMod(AnimatedExpandableListView expListView) {
        AnimatedExpandableListView l = expListView;
        int groupCount = l.getExpandableListAdapter().getGroupCount();
        System.out.println(groupCount);
        for (int i = 0; i < groupCount; i++) {
            View row = (View) l.getExpandableListAdapter().getGroupView(i, false, l, null);
            System.out.println(row);


            CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
            ImageView imageView = (ImageView) row.findViewById(R.id.showMoreMenu);
            check.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
//
//            if (row.getTag().equals("folder")) {
//                int childCount = l.getExpandableListAdapter().getChildrenCount(i);
//                for (int j = 0; j < childCount; j++) {
//                    ViewGroup childRow = (ViewGroup) l.getExpandableListAdapter().getChildView(i,j,false,expListView,row);
////                    groupPosition, childPosition, isLastChild, convertView, parent
//
//                    CheckBox childCheck = (CheckBox) childRow.findViewById(R.id.checkbox);
//                    ImageView childImageView = (ImageView) childRow.findViewById(R.id.showMoreMenu);
//                    childCheck.setVisibility(View.VISIBLE);
//                    childImageView.setVisibility(View.INVISIBLE);
//
//                }
//            }
        }
    }
}
