package com.example.hp.xmoblie.Items;

import java.util.List;

/**
 * Created by HP on 2017-10-24.
 */

public class OCRWordsDataItem {
    String boundingBox;
    List<OCRWordDataItem> words;

    public String getBoundingBox() {
        return boundingBox;
    }

    public List<OCRWordDataItem> getWords() {
        return words;
    }
}
