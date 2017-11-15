package com.example.hp.xmoblie.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.xmoblie.Holder.FileItemHolder;
import com.example.hp.xmoblie.Holder.ShortCutItemHolder;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.ShortCutItem;
import com.example.hp.xmoblie.R;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017-11-15.
 */

public class ShortCutListAdapter extends BaseAdapter {

    private Context _context;
    private List<ShortCutItem> _listDataHeader; // header titles
    private List<View> listSaver = new ArrayList<>();

    public ShortCutListAdapter(Context context, List<ShortCutItem> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        mackView();
    }

    @Override
    public int getCount() {
        return _listDataHeader.size();
    }

    @Override
    public ShortCutItem getItem(int i) {
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

    public void mackView() {
        int count = this.getCount();

        for (int i = 0; i < count; i++) {
            ShortCutItemHolder shortCutItemHolder = new ShortCutItemHolder();
            String headerTitle = (String) getItem(i).getFilename();


            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = infalInflater.inflate(R.layout.shortcut_list_low, null);
            shortCutItemHolder.fFileName = (TextView) view.findViewById(R.id.fileName);
            shortCutItemHolder.fFileIcon = (ImageView) view.findViewById(R.id.fileIcon);
            shortCutItemHolder.realShortCutItem = getItem(i);
            view.setTag(shortCutItemHolder);


            shortCutItemHolder.fFileName.setText(headerTitle);

            String fileFormat = FilenameUtils.getExtension(shortCutItemHolder.realShortCutItem.getFilename());
            switch (fileFormat.toLowerCase()) {
                case "7zip":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_7zip);
                    break;
                case "alz":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_alz);
                    break;
                case "avi":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_avi);
                    break;
                case "bmp":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_bmp);
                    break;
                case "c":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_c);
                    break;
                case "cpp":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_cpp);
                    break;
                case "cs":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_cs);
                    break;
                case "dll":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_dll);
                    break;
                case "doc":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_doc);
                    break;
                case "egg":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_egg);
                    break;
                case "exe":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_exe);
                    break;
                case "gif":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_gif);
                    break;
                case "h":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_h);
                    break;
                case "hwp":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_hwp);
                    break;
                case "jpg":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_jpg);
                    break;
                case "js":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_js);
                    break;
                case "mkv":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_mkv);
                    break;
                case "mp3":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_mp3);
                    break;
                case "mp4":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_mp4);
                    break;
                case "pdf":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_pdf);
                    break;
                case "png":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_png);
                    break;
                case "ppt":
                case "pptx":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_ppt);
                    break;
                case "ps1":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_ps1);
                    break;
                case "smi":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_smi);
                    break;
                case "srt":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_srt);
                    break;
                case "tar":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_tar);
                    break;
                case "txt":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_txt);
                    break;
                case "wma":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_wma);
                    break;
                case "wmv":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_wmv);
                    break;
                case "xlsx":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_xlsx);
                    break;
                case "zip":
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.fileicon_zip);
                    break;
                default:
                    shortCutItemHolder.fFileIcon.setImageResource(R.drawable.file);
                    break;
            }

            if (!listSaver.contains(view)) {
                listSaver.add(view);
            }
        }
    }
}
