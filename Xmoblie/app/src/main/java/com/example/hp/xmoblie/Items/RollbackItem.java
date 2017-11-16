package com.example.hp.xmoblie.Items;

import org.apache.commons.io.FileUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by HP on 2017-11-06.
 */

public class RollbackItem {
    String rname;
    int size;
    String date;

    public int getSize() {
        return size;
    }
    public String getReadAbleSize(){return FileUtils.byteCountToDisplaySize((long)getSize());}
    public String getPastDate() {
        date = date.replace("T"," ");
        System.out.println(date);

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
//        date = date.replace("T"," ");
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        formatter.setTimeZone(TimeZone.getDefault());
//
//        try{
//            Date gdate = formatter.parse(date);
//            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String localDate= formatter.format(gdate);
//
//            return localDate;
//        }catch (ParseException e){
//            e.printStackTrace();
//            return "";
//        }
    }
    public String getDate() {
        return date;
    }

    public String getRname() {
        return rname;
    }

}
