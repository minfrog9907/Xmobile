package com.example.hp.xmoblie.Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HP on 2017-10-16.
 */

public class FileItem {
    String filename;
    long size;
    String owner;
    int ggid,gid;
    String CreateDate;
    String LastWriteDate;
    int type;
    String path;
    String tag;

    public int getGgid() {
        return ggid;
    }

    public int getGid() {
        return gid;
    }

    public int getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public Date getCreateDate() throws ParseException {
        CreateDate.replace("T"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date now = formatter.parse(CreateDate);
        return now;
    }

    public String getFilename() {
        return filename;
    }

    public Date getLastWriteDate() throws ParseException {
        LastWriteDate.replace("T"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date now = formatter.parse(LastWriteDate);
        return now;
    }

    public String getOwner() {
        return owner;
    }

    public String getPath() {
        return path;
    }

    public String getTag() {
        return tag;
    }
}
