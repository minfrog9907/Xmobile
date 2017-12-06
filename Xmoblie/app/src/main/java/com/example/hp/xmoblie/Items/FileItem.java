package com.example.hp.xmoblie.Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by HP on 2017-10-16.
 */

public class FileItem {
    int idx;
    String filename;
    String displayName;
    String rname;
    long size;
    String userid;
    int ggid, gid;
    String CreateDate;
    String LastWriteDate;
    String vDir;
    String rDir;
    int type;
    int status;
    String del;
    boolean isShortCut;
    ArrayList<String> tags;

    public int getGgid() {
        System.out.println(ggid);
        return ggid;
    }

    public int getGid() {
        System.out.println(gid);
        return gid;
    }

    public int getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public Date getCreateDate() {
        if (CreateDate != null) {
            String date = CreateDate.replace("T", " ");

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Date now = dateFormatGmt.parse(date);
                return now;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParseCreateDate() {
        if (CreateDate != null) {
            String date = CreateDate.replace("T", " ");

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            try {
                Date gmttime = dateFormatGmt.parse(date);
                String formatterd = formatter.format(gmttime);

                return formatterd;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getFilename() {
        return filename;
    }

    public Date getLastWriteDate() {
        if (LastWriteDate != null) {
            String date = LastWriteDate.replace("T", " ");

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Date now = dateFormatGmt.parse(date);
                return now;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getParseLastWriteDate() {
        if (LastWriteDate != null) {
            String date = LastWriteDate.replace("T", " ");

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            try {
                Date gmttime = dateFormatGmt.parse(date);
                String formatterd = formatter.format(gmttime);

                return formatterd;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getUserid() {
        return userid;
    }

    public boolean getIsShortCut() {
        return isShortCut;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
