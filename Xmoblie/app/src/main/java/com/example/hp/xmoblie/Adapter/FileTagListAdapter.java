package com.example.hp.xmoblie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.xmoblie.Holder.TagItemHolder;
import com.example.hp.xmoblie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017-11-20.
 */

public class FileTagListAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<String> tags = new ArrayList<String>();
    private List<View> listSaver = new ArrayList<>();

    public FileTagListAdapter(Context context, ArrayList<String> tags) {
        this._context = context;
        this.tags = tags;
        mackView();
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public String getItem(int i) {
        return tags.get(i);
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

    public void mackView() {
        for (String tag : tags) {
            TagItemHolder tagItemHolder = new TagItemHolder();

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = infalInflater.inflate(R.layout.tag_list_low, null);
            tagItemHolder.tag = (TextView) view.findViewById(R.id.tag_list_row_TagName);

            tagItemHolder.tag.setText(tag);

            view.setTag(tag);

            if (!listSaver.contains(view)) {
                listSaver.add(view);
            }
        }

    }
}

