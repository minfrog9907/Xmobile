package com.example.hp.xmoblie.Items;

import java.util.List;

/**
 * Created by HP on 2017-10-24.
 */

public class OCRDataItem {
    String language;
    double textAngle;
    String orientation;
    List<OCRLineDataItem> regions;

    public double getTextAngle() {
        return textAngle;
    }

    public List<OCRLineDataItem> getRegions() {
        return regions;
    }

    public String getLanguage() {
        return language;
    }

    public String getOrientation() {
        return orientation;
    }

}
