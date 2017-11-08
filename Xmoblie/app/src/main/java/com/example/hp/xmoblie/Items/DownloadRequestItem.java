package com.example.hp.xmoblie.Items;

/**
 * Created by HP on 2017-11-08.
 */

public class DownloadRequestItem {
    int type;
    int length;

    long offset;

    String filename;
    String path;

    public DownloadRequestItem(int type, String filename, String path, long offset, int length) {
        this.type=type;
        this.filename=filename;
        this.path=path;
        this.offset=offset;
        this.length=length;
    }

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public long getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getType() {
        return type;
    }
}
