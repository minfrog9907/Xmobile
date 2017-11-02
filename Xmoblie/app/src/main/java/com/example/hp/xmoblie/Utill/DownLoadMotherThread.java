package com.example.hp.xmoblie.Utill;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

public class DownLoadMotherThread extends Thread {

    ArrayList<DownloadThread> downloadThreads = new ArrayList<DownloadThread>();
    ArrayList<ResponseBody> repResponseBodies = new ArrayList<ResponseBody>();
    byte[] downloadFile;
    String filename;
    int left;
    int thCnt = 0;
    int nowRunning = 0;
    int run = 0;
    int len;

    public void run(int type, String filename, String path, String token, long offset, int length) throws IOException {
        this.filename = filename;
        len = length;
        left = length;

        while (left > 0) {
            DownloadThread dt = new DownloadThread();
            length = left > 4096 ? 4096 : left;
            dt.dataSet(type, filename, path, token, 4096 * thCnt + offset, length, thCnt, this);
            downloadThreads.add(dt);
            left -= 4096;
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
           // Log.e("add", "add!" + thCnt);
            thCnt++;
        }

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
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            Log.e("size", repResponseBodies.size() + "");
            for (int i = 0; i < repResponseBodies.size(); ++i) {
               // Log.e("running","now "+i);
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
        if (this != null && this.isAlive())
            this.stop();
    }

    public synchronized void setResponseBody(ResponseBody responseBody,int id) throws IOException {
     //  Log.e("pakit",id+" "+responseBody.bytes().length+"  "+offset);
        repResponseBodies.set(id, responseBody);
    }

    public synchronized void reportDead(int id) throws IOException {
        nowRunning--;
        if (run == thCnt&&nowRunning==0) {
            saveImage();
//            for (int i = 0; i < repResponseBodies.size(); ++i) {
//                if(repResponseBodies.get(i).getResponseBody()==null){
//                    Log.e("check", i+"= null");
//                }
//                else
//                    Log.e("check", i+"= yes");
//            }

        } else if(run<thCnt){
            downloadThreads.get(run++).start();
            nowRunning++;
        }
       // Log.e("conThread",run + " " + thCnt +" "+ nowRunning);
    }
    private byte[] appendByteArray(byte[] original ,byte[] add) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write( original );
        outputStream.write( add );

        return outputStream.toByteArray( );
    }


}
