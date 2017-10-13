package com.example.hp.xmoblie.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.xmoblie.R;

/**
 * Created by HP on 2017-09-27.
 */

public class CustomActionbar extends LinearLayout {
    LinearLayout cabtnLay;
    ImageView caicon,cabtn;
    TextView catitle;


    public CustomActionbar(Context context) {
        super(context);
        initView();
    }
    public CustomActionbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }
    public CustomActionbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }
    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_actionbar, this, false);
        addView(v);
        caicon = (ImageView) findViewById(R.id.actionbar_icon);
        catitle = (TextView) findViewById(R.id.actionbar_title);
        cabtn = (ImageView) findViewById(R.id.actionbar_btn_img);
        cabtnLay = (LinearLayout) findViewById(R.id.actionbar_btn);

    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomActionbar);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomActionbar, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        DisplayMetrics scale = getContext().getResources().getDisplayMetrics();
        int symbol_resID = typedArray.getResourceId(R.styleable.CustomActionbar_caicon, R.drawable.file);
        caicon.setImageResource(symbol_resID);

        String text_string = typedArray.getString(R.styleable.CustomActionbar_catitle);
        catitle.setText(text_string);

        boolean showbtn = typedArray.getBoolean(R.styleable.CustomActionbar_showcabtn,false);
        if(showbtn){
            int btn_resID = typedArray.getResourceId(R.styleable.CustomActionbar_cabtn, R.drawable.file);
            int btn_rotation = typedArray.getInt(R.styleable.CustomActionbar_cabtnrotation, 0);
            cabtn.setImageResource(btn_resID);
            cabtn.setRotation(btn_rotation);
            cabtnLay.setVisibility(VISIBLE);
        }

        typedArray.recycle();
    }

    void setIcon(int symbol_resID) {
        caicon.setImageResource(symbol_resID);
    }
    void setText(String text_string) {
        catitle.setText(text_string);
    }
    void setText(int text_resID) {
        catitle.setText(text_resID);
    }
}


