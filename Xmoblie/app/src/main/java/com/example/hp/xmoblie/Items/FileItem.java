package com.example.hp.xmoblie.Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by HP on 2017-10-16.
 */

public class FileItem {
    int idx;
    String filename;
    String rname;
    long size;
    String userid;
    int ggid,gid;
    String createDate;
    String lastWriteDate;
    String vDir;
    String rDir;
    int type;
    int status;
    String del;
    boolean isShortCut;

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
        createDate.replace("T"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date now = formatter.parse(createDate);
        return now;
    }

    public String getParseCreateDate(){
        String date = createDate.replace("T"," ");

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        try{
            Date gmttime = dateFormatGmt.parse(date);
            String formatterd = formatter.format(gmttime);

            return formatterd;
        }catch (ParseException e){
            e.printStackTrace();
            return "";
        }
    }

    public String getFilename() {
        return filename;
    }

    public Date getLastWriteDate() throws ParseException {
        lastWriteDate.replace("T"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date now = formatter.parse(lastWriteDate);
        return now;
    }

    public String getParseLastWriteDate(){
        String date = lastWriteDate.replace("T"," ");

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        try{
            Date gmttime = dateFormatGmt.parse(date);
            String formatterd = formatter.format(gmttime);

            return formatterd;
        }catch (ParseException e){
            e.printStackTrace();
            return "";
        }
    }

    public String getUserid() {
        return userid;
    }

    public boolean getIsShortCut(){
        return isShortCut;
    }

}
