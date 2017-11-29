package com.example.hp.xmoblie.Thread;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hp.xmoblie.Service.DownloadManagerService;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Utill.NotificationHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by HP on 2017-10-31.
 */

public class DownloadMotherThread extends Thread {
    private int LENGTH =37268;
    private int MAXTHREAD=1;
    private int len;
    private int left;
    private int run = 0;
    private int thCnt = 0;
    private int nowRunning = 0;
    private String filename;
    private String root;
    private DownloadManagerService dm;
    private NotificationHandler handler;
    private FileOutputStream out;
    private File file;

    private ArrayList<DownloadThread> downloadThreads = new ArrayList<DownloadThread>();
    private ArrayList<ResponseBody> repResponseBodies = new ArrayList<ResponseBody>();

    public void run(int type, String filename, String path, String token, long offset, int length, DownloadManagerService dm) throws IOException {
        this.filename = filename;
        this.dm =dm;
        LENGTH  =length;
        len = length;
        left = length;

        handler = ServiceControlCenter.getInstance().getNotificationBarService().addService();
        handler.setName(filename);

        root = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/XmobileDownLoad/";
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = filename;

        file = new File(myDir, fname);
        out = new FileOutputStream(file);

        if (file.exists()) file.delete();

        while (left > 0) {
            DownloadThread dt = new DownloadThread();

            length = left > LENGTH ? LENGTH : left;
            dt.dataSet(type, filename, path, token, LENGTH * thCnt + offset, length, thCnt, this);
            downloadThreads.add(dt);

            left -= LENGTH;

            repResponseBodies.add(new ResponseBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public long contentLength() {
                    return 0;
                }

                @Override
                public BufferedSource source() {
                    return null;
                }
            });
            thCnt++;
        }

        Message message = handler.obtainMessage();
        message.what =200;
        message.arg1=thCnt;
        handler.sendMessage(message);


        Log.e("downloadind","start");
        for (int i = nowRunning; i < MAXTHREAD; ++i) {
            if (run < thCnt) {
                downloadThreads.get(run++).run();
                nowRunning++;
            }
        }

    }

    private void saveImage() {

        try {
            out.flush();
            out.close();
            Log.e("finish","finish");

            Message message = handler.obtainMessage();
            message.what =222;
            handler.setPath(root);
            handler.setName(filename);
            handler.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IO","IOex");
            return;
        }
        for (int i=0; i<downloadThreads.size(); ++i){
            if (downloadThreads.get(i) != null && downloadThreads.get(i).isAlive())
                downloadThreads.get(i).stop();
        }
        dm.dead();

    }

    public synchronized void setResponseBody(ResponseBody responseBody,int id) throws IOException {
       out.write(responseBody.bytes());
    }

    public synchronized void reportDead(int id) throws IOException {
        nowRunning--;
        downloadThreads.get(id).interrupt();
        Message message = handler.obtainMessage();
        message.what =100;
        message.arg1=thCnt;
        message.arg2=run;
        handler.sendMessage(message);


        if (run == thCnt&&nowRunning==0) {
            saveImage();
        } else if(run<thCnt){
            downloadThreads.get(run++).run();
            nowRunning++;
        }

    }
    public synchronized void badRepoDie(int id){
        downloadThreads.get(id).interrupt();
        dm.dead();
        ServiceControlCenter.getInstance().downloadFinish();
        Message message = handler.obtainMessage();
        message.what =333;
        message.arg1=run;
        handler.sendMessage(message);
    }
    public void freeForChild(){
        for (int i =0; i< downloadThreads.size();++i){
            if(downloadThreads.get(i).isAlive())
                downloadThreads.get(i).interrupt();
        }
        this.interrupt();
    }

    public String getFilename() {
        return filename;
    }

    public void recall(int id){
        Log.e("recall","recall");
        downloadThreads.get(id).run();
    }

}
