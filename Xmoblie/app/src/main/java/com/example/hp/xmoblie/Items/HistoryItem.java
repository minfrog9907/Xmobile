package com.example.hp.xmoblie.Items;

/**
 * Created by HP on 2017-11-16.
 */

public class HistoryItem {
    String path;
    FileItem fileItem;

    public HistoryItem(String path, FileItem fileItem) {
        this.path=path;
        this.fileItem = fileItem;
    }

    public String getPath() {
        return path;
    }

    public FileItem getFileItem() {
        return fileItem;
    }
}
