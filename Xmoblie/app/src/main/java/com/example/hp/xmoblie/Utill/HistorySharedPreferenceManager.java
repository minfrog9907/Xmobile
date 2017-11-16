package com.example.hp.xmoblie.Utill;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.HistoryItem;
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

    public boolean addHistroy(String path, FileItem fileItem) {
        cnt = history.getInt("cnt", -1);
        pushFront(convertItemToString(new HistoryItem(path,fileItem)));
        return true;
    }

    public String convertItemToString(HistoryItem historyItem) {
        Gson gson = new Gson();
        return gson.toJson(historyItem);
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
    public void printAll(){
        for(int i =0; i<10; ++i){
            Log.e("PA",history.getString("hs"+i,"empty"));
        }
    }

    public static HistorySharedPreferenceManager getInstance() {
        if (instance == null)
            return instance = new HistorySharedPreferenceManager();
        else
            return instance;
    }
}
