package com.example.hp.xmoblie.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-10-25.
 */

public class CustomFilemanagerBtnGroup extends LinearLayout{
    private boolean MultiCheck = false;
    private AttributeSet attr;
    private int defAttr;
    private final float scale = getContext().getResources().getDisplayMetrics().density;

    private View v;

    public CustomFilemanagerBtnGroup(Context context) {
        super(context);
        initView();
    }
    public CustomFilemanagerBtnGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
        attr = attrs;
    }
    public CustomFilemanagerBtnGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs,defStyleAttr);
        attr = attrs;
        defAttr = defStyleAttr;
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        v = li.inflate(R.layout.custom_filemanager_btn_group, this, false);
        addView(v);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilemanagerBtnGroup);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilemanagerBtnGroup, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        if(typedArray.getBoolean(R.styleable.CustomFilemanagerBtnGroup_isMultiMod,false)){
            setMultiMod();
        }else{
            setSingleMod();
        }
        typedArray.recycle();
    }

    private void setMultiMod(){
        LinearLayout customFilemanagerBtnGroup = (LinearLayout)v.findViewById(R.id.CustomFilemanagerBtnGroup);
        customFilemanagerBtnGroup.getLayoutParams().height = (int) (150 * scale + 5.0f);
        int childCount = customFilemanagerBtnGroup.getChildCount();
        for(int i = 0; i < childCount; i++){
            LinearLayout view = (LinearLayout) customFilemanagerBtnGroup.getChildAt(i);
            if(view.getTag() != null && view.getTag().equals("cfbRow")){
                if(view.getId() == R.id.hideable){
                    view.setVisibility(GONE);
                }
                int rowChildCount = customFilemanagerBtnGroup.getChildCount();
                for(int j = 0; j < rowChildCount; j++){
                    CustomFilemanagerBtn customFilemanagerBtn = (CustomFilemanagerBtn) view.getChildAt(j);
                    if(!customFilemanagerBtn.getMultiState()){
                        customFilemanagerBtn.setVisibility(GONE);
                    }
                }
            }
        }

    }

    private void setSingleMod(){
        LinearLayout customFilemanagerBtnGroup = (LinearLayout)v.findViewById(R.id.CustomFilemanagerBtnGroup);
        customFilemanagerBtnGroup.getLayoutParams().height = (int) (250 * scale + 5.0f);
        int childCount = customFilemanagerBtnGroup.getChildCount();
        for(int i = 0; i < childCount; i++){
            LinearLayout view = (LinearLayout) customFilemanagerBtnGroup.getChildAt(i);
            if(view.getTag() != null && view.getTag().equals("cfbRow")){
                view.setVisibility(VISIBLE);
                int rowChildCount = customFilemanagerBtnGroup.getChildCount();
                for(int j = 0; j < rowChildCount; j++){
                    CustomFilemanagerBtn customFilemanagerBtn = (CustomFilemanagerBtn) view.getChildAt(j);
                    customFilemanagerBtn.setVisibility(VISIBLE);
                }
            }
        }
    }

    public void setMultiMod(boolean multiMod){
        if(multiMod){
            setMultiMod();
        }else{
            setSingleMod();
        }
    }
}
