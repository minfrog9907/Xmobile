package com.example.hp.xmoblie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.xmoblie.Holder.StarItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.StarItem;
import com.example.hp.xmoblie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim doyeon on 2017-11-01.
 */

public class StarItemAdapter extends BaseAdapter {

    private Context _context;
    private List<StarItem> _list; // header titles
    private List<View> listSaver = new ArrayList<>();

    public StarItemAdapter(Context context, List<StarItem> _list) {
        this._context = context;
        this._list = _list;
        mackView();
    }

    @Override
    public int getCount() {
        return _list.size();
    }

    @Override
    public StarItem getItem(int i) {
        return _list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return listSaver.get(i);
    }

    public View getViewat(int i) {
        return listSaver.get(i);
    }

    public void mackView() {
        int count = this.getCount();

        for(int i = 0; i < count; i++) {
            StarItemHolder starItemHolder = new StarItemHolder();
            String fileTitle = (String) getItem(i).getFilename();

            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.file_list_row, null);
            starItemHolder.FileName = (TextView) view.findViewById(R.id.fileName);

            view.setTag(starItemHolder);
            ImageView showMoreMenu = (ImageView) view.findViewById(R.id.showMoreMenu);
            showMoreMenu.setVisibility(View.INVISIBLE);

            starItemHolder.FileName.setText(fileTitle);

            if (!listSaver.contains(view)) {
                listSaver.add(view);
            }
        }
    }

}
