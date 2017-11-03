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

import org.apache.commons.io.FilenameUtils;

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

                String fileFormat = FilenameUtils.getExtension(fileItemHolder.realFileItem.getFilename());
                switch (fileFormat){
                    case "7zip" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_7zip);
                        break;
                    case "alz" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_alz);
                        break;
                    case "avi" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_avi);
                        break;
                    case "bmp" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_bmp);
                        break;
                    case "c" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_c);
                        break;
                    case "cpp" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_cpp);
                        break;
                    case "cs" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_cs);
                        break;
                    case "dll" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_dll);
                        break;
                    case "doc" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_doc);
                        break;
                    case "egg" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_egg);
                        break;
                    case "exe" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_exe);
                        break;
                    case "gif" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_gif);
                        break;
                    case "h" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_h);
                        break;
                    case "hwp" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_hwp);
                        break;
                    case "jpg" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_jpg);
                        break;
                    case "js" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_js);
                        break;
                    case "mkv" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_mkv);
                        break;
                    case "mp3" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_mp3);
                        break;
                    case "mp4" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_mp4);
                        break;
                    case "pdf" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_pdf);
                        break;
                    case "png" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_png);
                        break;
                    case "ppt" : case "pptx" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_ppt);
                        break;
                    case "ps1" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_ps1);
                        break;
                    case "smi" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_smi);
                        break;
                    case "srt" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_srt);
                        break;
                    case "tar" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_tar);
                        break;
                    case "txt" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_txt);
                        break;
                    case "wma" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_wma);
                        break;
                    case "wmv" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_wmv);
                        break;
                    case "xlsx" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_xlsx);
                        break;
                    case "zip" :
                        fileItemHolder.fileIcon.setImageResource(R.drawable.fileicon_zip);
                        break;
                    default:
                        fileItemHolder.fileIcon.setImageResource(R.drawable.file);
                        break;
                }

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
