package com.example.hp.xmoblie.Items;

import java.util.List;

/**
 * Created by HP on 2017-10-24.
 */

public class OCRLineDataItem {
    String boundingBox;
    List<OCRWordsDataItem> lines;

    public List<OCRWordsDataItem> getLines() {
        return lines;
    }

    public String getBoundingBox() {
        return boundingBox;
    }
}
