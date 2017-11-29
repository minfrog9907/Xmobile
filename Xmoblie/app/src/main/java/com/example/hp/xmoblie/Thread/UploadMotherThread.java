package com.example.hp.xmoblie.Thread;

import android.os.Message;
import android.util.Log;

import com.example.hp.xmoblie.Service.ApiClient;
import com.example.hp.xmoblie.Service.ServiceControlCenter;
import com.example.hp.xmoblie.Service.UploadManagerService;
import com.example.hp.xmoblie.Utill.NotificationHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-11-20.
 */

public class UploadMotherThread extends Thread {

    ApiClient apiClient = ApiClient.severService;
    private int LENGTH =10485760;
    private int MAXTHREAD=1;
    private long left;
    private int run = 0;
    private int thCnt = 0;
    private int nowRunning = 0;
    private long length;

    private byte[] filebyte;
    private String path;
    private String filename;
    private UploadManagerService um;
    private NotificationHandler handler;

    private ArrayList<UploadThread> uploadThreads = new ArrayList<UploadThread>();


    public void run(String filename, String toPath, String realPath,long size) throws IOException {
        Log.e("Upload", "업로드");

        this.filename = filename;
        this.path = toPath;
        left = size;
        filebyte = fileToByte(realPath);

        sendServerToState(8);

        handler = ServiceControlCenter.getInstance().getNotificationBarService().addService();
        handler.setName(filename);


        while (left > 0) {
            UploadThread ut = new UploadThread();

            length = left > LENGTH ? LENGTH : left;
            ut.setData(makeRequestBody(filename,path,LENGTH * thCnt,length),thCnt, this);
            uploadThreads.add(ut);

            left -= LENGTH;

            thCnt++;
        }

        Message message = handler.obtainMessage();
        message.what = 900;
        message.arg1 = thCnt;
        handler.sendMessage(message);



    }

    private byte[] stringToByte(int type, String a) {
        byte[] euckrStringBuffer;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(reverse(intToByteArray(type)));
            outputStream.write(a.getBytes(Charset.forName("euc-kr")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        euckrStringBuffer = outputStream.toByteArray();
        return euckrStringBuffer;

    }

    private byte[] fileToByte(String realPath) throws IOException {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        File file = new File(realPath+"/"+filename);

        length =file.length()/1024;

        bytesArray = new byte[(int) file.length()];

        //read file into bytes[]
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(bytesArray);
        return bytesArray;
    }

    public byte[] getFilebyteWithOffset(int offset, int length){
        byte[] tmp = new byte[length];
        System.arraycopy(filebyte,offset,tmp,0,length);
        return tmp;
    }

    private RequestBody makeRequestBody(String filename, String path,long offset,long length){
        Log.e("offset len",offset+" "+length);
        byte[] euckrStringBuffer;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(reverse(intToByteArray(5)));
            outputStream.write((filename + "\0" + path + "\0").getBytes(Charset.forName("euc-kr")));
            outputStream.write(reverse(longToBytes(offset)));
            outputStream.write(reverse(intToByteArray((int)length)));
            outputStream.write(getFilebyteWithOffset((int)offset,(int)length));
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

    private  byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
    private  byte[] reverse(byte[] array) {
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
    public synchronized void reportDead(int id) throws IOException {
        nowRunning--;
        uploadThreads.get(id).interrupt();
        Message message = handler.obtainMessage();
        message.what =990;
        message.arg1=thCnt;
        message.arg2=run;
        handler.sendMessage(message);


        if (run == thCnt&&nowRunning==0) {
            sendServerToState(10);
            ServiceControlCenter.getInstance().getUploadManagerService().dead();
            message = handler.obtainMessage();
            message.what =9999;
            handler.sendMessage(message);
        } else if(run<thCnt){
            uploadThreads.get(run++).run();
            nowRunning++;
        }

    }
    public synchronized void badRepoDie(int id){
        uploadThreads.get(id).interrupt();
        um.dead();
        ServiceControlCenter.getInstance().downloadFinish();
        Message message = handler.obtainMessage();
        message.what =999;
        message.arg1=run;
        handler.sendMessage(message);
    }

    public void recall(int id){
        Log.e("Upload","recall");
        uploadThreads.get(id).run();
    }
    public void freeForChild(){
        for (int i =0; i< uploadThreads.size();++i){
            if(uploadThreads.get(i).isAlive())
                uploadThreads.get(i).interrupt();
        }
        this.interrupt();
    }

    public String getFilename() {
        return filename;
    }

    private void sendServerToState(final int type) {
        Log.e("Upload", "업로드 send");
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), stringToByte(type, filename        + "\0" + path + "\0"));

        Call<ResponseBody> call = apiClient.repoUploadService(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type == 8) {
                    Log.e("Upload", "업로드 시작");
                    Log.e("Uploading", "start");
                    for (int i = nowRunning; i < MAXTHREAD; ++i) {
                        if (run < thCnt) {
                            uploadThreads.get(run++).run();
                            nowRunning++;
                        }
                    }
                }
                if (type == 10)
                    Log.e("Upload", "업로드 종료");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload", "연결실패");

            }
        });
    }


}
