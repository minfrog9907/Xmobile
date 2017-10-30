package com.example.hp.xmoblie.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-10-25.
 */

public class CustomFilemanagerBtn extends LinearLayout {

    private boolean MultiState = false;
    TextView text;
    ImageView icon;

    public CustomFilemanagerBtn(Context context) {
        super(context);
        initView();
    }
    public CustomFilemanagerBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }
    public CustomFilemanagerBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_filemanager_btn, this, false);
        addView(v);
        text = (TextView) findViewById(R.id.fileManagerBtnTxt);
        icon = (ImageView) findViewById(R.id.fileManagerBtnImg);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilemanagerBtn);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilemanagerBtn, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        int symbol_resID = typedArray.getResourceId(R.styleable.CustomFilemanagerBtn_cfbIcon, R.drawable.all);
        icon.setImageResource(symbol_resID);
        String text_string = typedArray.getString(R.styleable.CustomFilemanagerBtn_cfbText);
        text.setText(text_string);

        MultiState = typedArray.getBoolean(R.styleable.CustomFilemanagerBtn_cfbMulti,true);

        LinearLayout liner;
        if(typedArray.getBoolean(R.styleable.CustomFilemanagerBtn_cfbTop, false)){
            liner = (LinearLayout)findViewById(R.id.topLine);
            liner.setVisibility(VISIBLE);
        }

        if(typedArray.getBoolean(R.styleable.CustomFilemanagerBtn_cfbBottom, false)){
            liner = (LinearLayout)findViewById(R.id.bottomLine);
            liner.setVisibility(VISIBLE);
        }

        if(typedArray.getBoolean(R.styleable.CustomFilemanagerBtn_cfbLeft, false)){
            liner = (LinearLayout)findViewById(R.id.leftLine);
            liner.setVisibility(VISIBLE);
        }

        if(typedArray.getBoolean(R.styleable.CustomFilemanagerBtn_cfbRight, false)){
            liner = (LinearLayout)findViewById(R.id.rigthLine);
            liner.setVisibility(VISIBLE);
        }


        typedArray.recycle();
    }

    public boolean getMultiState(){
        return MultiState;
    }

}
