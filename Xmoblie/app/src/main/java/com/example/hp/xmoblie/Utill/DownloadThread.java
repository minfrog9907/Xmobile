package com.example.hp.xmoblie.Utill;

import android.util.Log;

import com.example.hp.xmoblie.Service.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-31.
 */

public class DownloadThread extends Thread {
    int type;
    int id;
    int length;
    int recallTime=0;
    long offset;

    String filename;
    String path;
    String token;

    DownloadMotherThread dm;

    public void run() {
        byte[] euckrStringBuffer;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(reverse(intToByteArray(type)));
            outputStream.write((filename + "\0" + path + "\0" + token + "\0").getBytes(Charset.forName("euc-kr")));
            outputStream.write(reverse(longToBytes(offset)));
            outputStream.write(reverse(intToByteArray(length)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        euckrStringBuffer = outputStream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), euckrStringBuffer);

        ApiClient apiClient = ApiClient.severService;

        Call<ResponseBody> call = apiClient.repoDownload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dm.setResponseBody(response.body(),id);
                    dm.reportDead(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (this != null && isAlive())
                    interrupt();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("downloadsss",t.getMessage());
                if(recallTime++!=4){
                    dm.recall(id);
                }
                if (recallTime==4){
                    dm.badRepoDie(id);
                }

            }
        });

    }

    public  void dataSet(int type, String filename, String path, String token, long offset, int length,int id,DownloadMotherThread dt){
        this.type=type;
        this.filename =filename;
        this.path =path;
        this.token=token;
        this.offset=offset;
        this.length=length;
        this.id=id;
        this.dm=dt;
    }

    private byte[] intToByteArray(final int integer) {
        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES);
        buff.putInt(integer);
        return buff.array();
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public byte[] reverse(byte[] array) {
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
        Log.e("kill","kill DT");
    }
}
