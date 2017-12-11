package com.example.hp.xmoblie.Thread;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hp.xmoblie.Items.PacketType;
import com.example.hp.xmoblie.Service.ApiClient;
import com.example.hp.xmoblie.Service.DownloadManagerService;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Utill.NotificationHandler;
import com.example.hp.xmoblie.Utill.SessionCall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-31.
 */

public class DownloadMotherThread extends Thread {
    private int LENGTH = 1048576;
    private int MAXTHREAD = 1;
    private int len;
    private int left;
    private int run = 0;
    private int thCnt = 0;
    private int nowRunning = 0;
    private boolean deadCall=false;
    private String filename;
    private String root;
    private DownloadManagerService dm;
    private NotificationHandler handler;

    private ApiClient apiClient;

    private ArrayList<DownloadThread> downloadThreads = new ArrayList<DownloadThread>();
    private ArrayList<ResponseBody> repResponseBodies = new ArrayList<ResponseBody>();
    FileOutputStream out;
    File file;

    public void run(final int type, String filename, final String path, final String token, final long offset, final int length, DownloadManagerService dm) throws IOException {
        root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/XmobileDownLoad/";

        File myDir = new File(root);
        myDir.mkdirs();

        String fname = filename;

        file = new File(myDir, fname);

        if (file.exists()) file.delete();
        out = new FileOutputStream(file);

        apiClient = ApiClient.severService;

        this.filename = filename;
        this.dm = dm;
        len = length;
        left = length;

        handler = ServiceControlCenter.getInstance().getNotificationBarService().addService();
        handler.setName(filename);




        Call<ResponseBody> call = apiClient.repoInitDownload(getByte(PacketType.PT_InitDownload.ordinal(), filename, path));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                downloadStart(type, length, path, token, offset);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    private void downloadStart(int type, int length, String path, String token, long offset) {
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
        message.what = 200;
        message.arg1 = thCnt;
        handler.sendMessage(message);


        Log.e("downloadind", "start");
        for (int i = nowRunning; i < MAXTHREAD; ++i) {
            if (run < thCnt) {
                downloadThreads.get(run++).run();
                nowRunning++;
            }
        }
    }

    private void saveImage() throws IOException {
        Log.e("finish", "finish");
        out.flush();
        out.close();

        Message message = handler.obtainMessage();
        message.what = 222;
        handler.setPath(root);
        handler.setName(filename);
        handler.sendMessage(message);

        for (int i = 0; i < downloadThreads.size(); ++i) {
            if (downloadThreads.get(i) != null && downloadThreads.get(i).isAlive())
                downloadThreads.get(i).stop();
        }
        dm.dead();

    }

    public synchronized void setResponseBody(ResponseBody responseBody, int id) throws IOException {
        try {
            out.write(responseBody.bytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IO", "IOex");
            return;
        }
    }

    public synchronized void reportDead(int id) throws IOException {
        nowRunning--;
        downloadThreads.get(id).interrupt();
        messageDownloadCall();
        if (run == thCnt && nowRunning == 0) {
            saveImage();
        } else if (run < thCnt) {
            downloadThreads.get(run++).run();
            nowRunning++;
        }

    }

    public synchronized void badRepoDie(int id) {
        downloadThreads.get(id).interrupt();
        dm.dead();
        ServiceControlCenter.getInstance().downloadFinish();
        messageFailCall();
    }

    public void freeForChild() {
        for (int i = 0; i < downloadThreads.size(); ++i) {
                downloadThreads.get(i).interrupt();
        }
        thCnt=0;

        deleteNowFile();
        messageCancelCall();
        ServiceControlCenter.getInstance().downloadFinish();
        this.interrupt();
    }

    public String getFilename() {
        return filename;
    }

    public void recall(int id) {
        Log.e("recall", "recall");
        downloadThreads.get(id).run();
    }

    private RequestBody getByte(int type, String filename, String path) {
        byte[] euckrStringBuffer;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            outputStream.write(reverse(intToByteArray(type)));//reverse(intToByteArray(type)));
            outputStream.write((filename + "\0" + path + "\0").getBytes(Charset.forName("euc-kr")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        euckrStringBuffer = outputStream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), euckrStringBuffer);
        return body;
    }

    private byte[] intToByteArray(final int integer) {
        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES);
        buff.putInt(integer);
        return buff.array();
    }

    private byte[] reverse(byte[] array) {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }
    @Override
    public void interrupt() {
        super.interrupt();
        Log.e("kill","kill DMT");
    }
    private void deleteNowFile(){
        root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/XmobileDownLoad/";

        File myDir = new File(root);
        myDir.mkdirs();

        String fname = filename;

        File file = new File(myDir, fname);

        if (file.exists()) file.delete();
    }
    private void messageCancelCall(){
        deadCall=true;
        Message message = handler.obtainMessage();
        message.what = 3333;
        handler.sendMessage(message);
    }
    private void messageFailCall(){
        Message message = handler.obtainMessage();
        message.what = 333;
        message.arg1 = run;
        handler.sendMessage(message);
    }

    private void messageDownloadCall(){
        if(!deadCall) {
            Message message = handler.obtainMessage();
            message.what = 100;
            message.arg1 = thCnt;
            message.arg2 = run;
            handler.sendMessage(message);
        }
    }
}
