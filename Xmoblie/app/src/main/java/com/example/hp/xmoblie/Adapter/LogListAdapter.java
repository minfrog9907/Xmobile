package com.example.hp.xmoblie.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Holder.LogItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.R;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017-11-13.
 */

public class LogListAdapter extends BaseAdapter {

    private Context _context;
    private List<RollbackItem> _listDataHeader; // header titles
    private List<View> listSaver = new ArrayList<>();

    public LogListAdapter(Context context, List<RollbackItem> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        System.out.println(_listDataHeader);
        mackView();
    }

    @Override
    public int getCount() {
        return _listDataHeader.size();
    }

    @Override
    public RollbackItem getItem(int i) {
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
            LogItemHolder logItemHolder = new LogItemHolder();
            String headerTitle = (String) getItem(i).getRname();
            String fsize = (String) getItem(i).getReadAbleSize();
            String fDate = (String) getItem(i).getPastDate();

            System.out.println(headerTitle + "     " + fsize + "        " + fDate);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = infalInflater.inflate(R.layout.log_list_row, null);
            logItemHolder.fileName = (TextView) view.findViewById(R.id.fileName);
            logItemHolder.fileSize = (TextView) view.findViewById(R.id.fileSize);
            logItemHolder.fileDate = (TextView) view.findViewById(R.id.fileDate);

            logItemHolder.fileName.setText(headerTitle);
            logItemHolder.fileSize.setText(fsize);
            logItemHolder.fileDate.setText(fDate);

            view.setTag(logItemHolder);

            if (!listSaver.contains(view)) {
                listSaver.add(view);
            }
        }
    }
}

