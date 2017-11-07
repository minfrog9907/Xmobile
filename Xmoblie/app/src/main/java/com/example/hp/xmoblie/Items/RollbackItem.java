package com.example.hp.xmoblie.Items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public Date getPastDate() throws ParseException {
        date.replace("T"," ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        Date now = formatter.parse(date);
        return now;
    }
    public String getDate() {
        return date;
    }

    public String getRname() {
        return rname;
    }
}
