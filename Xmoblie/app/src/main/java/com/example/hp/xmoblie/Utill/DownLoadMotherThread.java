package com.example.hp.xmoblie.Utill;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.xmoblie.Items.DownloadFileItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by HP on 2017-10-31.
 */

public class DownLoadMotherThread extends Thread {

    ArrayList<DownloadThread> downloadThreads = new ArrayList<DownloadThread>();
    ArrayList<DownloadFileItem> repResponseBodies = new ArrayList<DownloadFileItem>();
    ArrayList<DownloadFileItem> tmp = new ArrayList<DownloadFileItem>();
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
        downloadFile = new byte[length];

        while (left > 0) {
            DownloadThread dt = new DownloadThread();
            length = left > 4096 ? 4096 : left;
            dt.dataSet(type, filename, path, token, 4096 * thCnt + offset, length, thCnt, this);
            downloadThreads.add(dt);
            left -= 4096;
            Log.e("add", "add!" + thCnt);
            thCnt++;
        }
        if (run < thCnt) {
            for (int i = nowRunning; i < 2; ++i) {
                if (run < thCnt) {
                    downloadThreads.get(run++).run();
                }
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
            for (int i = 0; i < repResponseBodies.size(); ++i) {
                out.write(repResponseBodies.get(i).getResponseBody().bytes(), repResponseBodies.get(i).getOffset(), len);
            }
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (this != null && this.isAlive())
            this.stop();
    }

    public synchronized void setResponseBody(ResponseBody responseBody, long offset) throws IOException {
        // Log.e("pakit",responseBody.bytes().length+"  "+offset);
        repResponseBodies.add(new DownloadFileItem().setFile((int) offset, responseBody));
    }

    public synchronized void reportDead(int id) {
        if (run == thCnt) {
            saveImage();
        } else
            downloadThreads.get(run++).start();
    }

    public void makeByteArray() {
        int idx=0;
        int min = 0;
        for (int i = 0; i < repResponseBodies.size(); ++i) {
            for (int j = 0; j < repResponseBodies.size(); ++j) {
                if (repResponseBodies.get(j).getOffset() <= min) {
                    min = repResponseBodies.get(j).getOffset();
                    idx=j;
                }
            }
            tmp.add(repResponseBodies.get(idx));
            repResponseBodies.remove(idx);
        }
    }
    public byte[] appendByte(byte[] in, byte[] to){
        byte[] c = new byte[in.length + to.length];
        System.arraycopy(to, 0, c, 0, to.length);
        System.arraycopy(in, 0, c, to.length, in.length);
        return c;
    }

}
