package com.example.hp.xmoblie.Utill;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.google.gson.Gson;

/**
 * Created by HP on 2017-11-14.
 */

public class HistorySharedPreferenceManager {
    private static HistorySharedPreferenceManager instance;
    private SharedPreferences history;
    private SharedPreferences.Editor editor;
    private int cnt;

    public HistorySharedPreferenceManager() {
        history = ServiceControlCenter.getInstance().getContext().getSharedPreferences("date", Activity.MODE_PRIVATE);
        editor = history.edit();

        if (history.getInt("cnt", -1) == -1) {
            editor.putInt("cnt", 0);
            for (int i = 0; i < 10; ++i)
                editor.putString("hs" + i, "empty");

        }
    }

    public boolean addHistroy(FileItem fileItem) {
        cnt = history.getInt("cnt", -1);
        pushFront(convertItemToString(fileItem));
        return true;
    }

    public String convertItemToString(FileItem fileItem) {
        Gson gson = new Gson();
        return gson.toJson(fileItem);
    }

    private void pushFront(String convertString){
        for(int i=cnt; i>0; --i){
            if(i<10) {
                String tmp = history.getString("hs" + (i - 1), "empty");
                if (tmp != "empty") {
                    editor.putString("hs" + i, tmp);
                }
            }
        }
        editor.putString("hs0",convertString);
        editor.putInt("cnt", cnt++);
        editor.commit();
    }

    public static HistorySharedPreferenceManager getInstance() {
        if (instance == null)
            return instance = new HistorySharedPreferenceManager();
        else
            return instance;
    }
}
