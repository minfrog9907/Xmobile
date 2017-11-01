package com.example.hp.xmoblie.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 2017-10-31.
 */

public class FileManagerListAdapter extends BaseAdapter {

    private Context _context;
    private List<FileItem> _listDataHeader; // header titles
    private List<View> listSaver = new ArrayList<>();

    public FileManagerListAdapter(Context context, List<FileItem> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        mackView();
    }

    @Override
    public int getCount() {
        return _listDataHeader.size();
    }

    @Override
    public FileItem getItem(int i) {
        return _listDataHeader.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return listSaver.get(i);
    }

    public View getViewAt(int i) {
        return listSaver.get(i);
    }

    public void mackView(){
        int count = this.getCount();

        for(int i = 0; i < count; i++){
            FileItemHolder fileItemHolder = new FileItemHolder();
            String headerTitle = (String) getItem(i).getFilename();
            int type = (int) getItem(i).getType();


            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = infalInflater.inflate(R.layout.file_list_row, null);
            fileItemHolder.fileName = (TextView) view.findViewById(R.id.fileName);
            fileItemHolder.fileIcon = (ImageView) view.findViewById(R.id.fileIcon);
            fileItemHolder.showMoreMenu = (ImageView) view.findViewById(R.id.showMoreMenu);
            fileItemHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            fileItemHolder.realFileItem = getItem(i);
            view.setTag(fileItemHolder);


            fileItemHolder.fileName.setText(headerTitle);
            fileItemHolder.fileName.setTypeface(null, Typeface.BOLD);

            if (type == 128) {
                fileItemHolder.showMoreMenu.setVisibility(View.INVISIBLE);
                fileItemHolder.fileIcon.setImageResource(R.drawable.file);
                fileItemHolder.fileIcon.setTag("file");
            } else {
                fileItemHolder.fileIcon.setImageResource(R.drawable.folder);
                fileItemHolder.fileIcon.setTag("folder");
            }

            if (!listSaver.contains(view)) {
                listSaver.add(view);
            }
        }
    }
}
