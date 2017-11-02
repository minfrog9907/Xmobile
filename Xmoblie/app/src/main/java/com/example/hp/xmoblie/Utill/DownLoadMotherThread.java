package com.example.hp.xmoblie.Utill;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

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
    int LENGTH =4000;
    int len;
    int left;
    int run = 0;
    int thCnt = 0;
    int nowRunning = 0;
    String filename;
    DownloadManager dm;

    ArrayList<DownloadThread> downloadThreads = new ArrayList<DownloadThread>();
    ArrayList<ResponseBody> repResponseBodies = new ArrayList<ResponseBody>();

    public void run(int type, String filename, String path, String token, long offset, int length, DownloadManager dm) throws IOException {
        this.filename = filename;
        this.dm =dm;

        len = length;
        left = length;

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
        Log.e("downloadind","start");
        for (int i = nowRunning; i < 2; ++i) {
            if (run < thCnt) {
                downloadThreads.get(run++).run();
                nowRunning++;
            }
        }

    }

    private void saveImage() {
        String root = Environment.getExternalStorageDirectory().toString() + "/XMobileDownLoad/";
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = filename;

        File file = new File(myDir, fname);

        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            for (int i = 0; i < repResponseBodies.size(); ++i) {
                out.write(repResponseBodies.get(i).bytes());
            }
            out.flush();
            out.close();
            Log.e("finish","finish");

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
        repResponseBodies.set(id, responseBody);
    }

    public synchronized void reportDead(int id) throws IOException {
        nowRunning--;
        if (run == thCnt&&nowRunning==0) {
            saveImage();
        } else if(run<thCnt){
            downloadThreads.get(run++).start();
            nowRunning++;
        }

    }

    public int finishedPakitCNT(){
        return run+1;
    }


}