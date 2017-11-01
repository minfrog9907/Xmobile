package com.example.hp.xmoblie.Items;

import okhttp3.ResponseBody;

/**
 * Created by HP on 2017-11-01.
 */

public class DownloadFileItem {
    ResponseBody responseBody;
    int offset;
    public DownloadFileItem setFile(int offset,ResponseBody responseBody){
        this.offset = offset;
        this.responseBody = responseBody;
        return this;
    }


    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }


    public int getOffset() {
        return offset;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }
}
