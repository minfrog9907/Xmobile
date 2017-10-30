package com.example.hp.xmoblie.Holder;

import com.example.hp.xmoblie.Items.FileItem;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by HP on 2017-10-27.
 */

public class FileitemGroup extends ExpandableGroup<FileItem> {
    public FileitemGroup(String title, List<FileItem> items) {
        super(title, items);
    }
}
