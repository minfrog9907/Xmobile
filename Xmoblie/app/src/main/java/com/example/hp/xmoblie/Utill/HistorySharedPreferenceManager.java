package com.example.hp.xmoblie.Utill;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.HistoryItem;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by HP on 2017-11-14.
 */

public class HistorySharedPreferenceManager {
    private static HistorySharedPreferenceManager instance;
    private SharedPreferences history;
    private SharedPreferences.Editor editor;
    private int cnt;

    public HistorySharedPreferenceManager() {
        history = ServiceControlCenter.getInstance().getContext().getSharedPreferences("historyTest", Activity.MODE_PRIVATE);
        editor = history.edit();
//        editor.clear();
//        editor.commit();
        if (history.getInt("cnt", -1) == -1) {
            editor.putInt("cnt", 0);
            for (int i = 0; i < 10; ++i)
                editor.putString("hs" + i, "empty");
            editor.commit();
        }
        cnt = history.getInt("cnt", -1);
    }

    public boolean addHistroy(String path, FileItem fileItem) {
        pushFront(convertItemToString(new HistoryItem(path, fileItem)));
        return true;
    }

    private String convertItemToString(HistoryItem historyItem) {
        Gson gson = new Gson();
        return gson.toJson(historyItem);
    }

    private void pushFront(String convertString) {
        if (!findSameHistory(convertString)) {
            Log.e("PA", "INSERT");
            Log.e("PA", "--------------------------------------------------------");
            push(convertString,1);
            printAll();
        }
    }

    private void push(String convertString,int oRc) {
        for (int i = cnt; i > 0; --i) {
            if (i < 10) {
                String tmp = history.getString(new String("hs" + (i - 1)), "empty");
                if (!tmp.equals("empty")) {
                    editor.putString("hs" + i, tmp);
                }

            }

        }
        cnt+=oRc;
        Log.e("cnt", cnt+"");
        editor.putString("hs0", convertString);
        editor.putInt("cnt",cnt );
        editor.commit();

    }

    private boolean findSameHistory(String convertString) {
        for (int i = 0; i < cnt; ++i) {
            if (convertString.equals(history.getString("hs" + i, "empty"))) {
                push(convertString,1);
                grabageCollect(i+1);
                return true;
            }
        }
        return false;
    }

    private void grabageCollect(int now) {
        Log.e("PA", "COLLECTED");
        Log.e("PA", "--------------------------------------------------------");
        for (int i = now; i < cnt; ++i) {
            editor.putString("hs" + i, history.getString("hs" + (i + 1), "empty"));
        }
        editor.putInt("cnt",--cnt);
        editor.commit();
        printAll();

    }

    public boolean deleteHistory(String path,FileItem fileItem){
        String convertString = convertItemToString(new HistoryItem(path,fileItem));
        for (int i = 0; i < 10; ++i) {
            if (convertString.equals(history.getString("hs" + i, "empty"))) {
                grabageCollect(i + 1);
                return true;
            }
        }
        return false;
    }
    public void printAll() {
        for (int i = 0; i <cnt; ++i) {
            Log.e("PA",i+" :  "+ history.getString("hs" + i, "empty"));
        }
        Log.e("PA", "--------------------------------------------------------");

    }

    public ArrayList<HistoryItem> getHistory(){
        ArrayList<HistoryItem> historyItems = new ArrayList<HistoryItem>();
        int aa=0;
        for (int i=0; i<cnt; ++i){
            String json=history.getString("hs"+i,"empty");
            if(!json.equals("empty")) {
                Gson gson = new Gson();
                HistoryItem hi = gson.fromJson(json, HistoryItem.class);
                historyItems.add(hi);
                aa++;
            }
        }
        editor.putInt("cnt",aa);
        return historyItems;
    }

    public static HistorySharedPreferenceManager getInstance() {
        if (instance == null)
            return instance = new HistorySharedPreferenceManager();
        else
            return instance;
    }
}