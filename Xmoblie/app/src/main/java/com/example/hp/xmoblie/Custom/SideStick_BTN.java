package com.example.hp.xmoblie.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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

public class SideStick_BTN extends LinearLayout {
    LinearLayout stick;
    ImageView symbol;
    TextView text;

    public SideStick_BTN(Context context) {
        super(context);
        initView();
    }
    public SideStick_BTN(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }
    public SideStick_BTN(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }
    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_btn_sidebtn, this, false);
        addView(v);
        symbol = (ImageView) findViewById(R.id.img_icon);
        text = (TextView) findViewById(R.id.img_text);
        stick = (LinearLayout)findViewById(R.id.img_stick);

    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideStick_BTN);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideStick_BTN, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        int symbol_resID = typedArray.getResourceId(R.styleable.SideStick_BTN_symbol, R.drawable.file);
        symbol.setImageResource(symbol_resID);
        String text_string = typedArray.getString(R.styleable.SideStick_BTN_text);
        text.setText(text_string);
        Boolean stick_bool = typedArray.getBoolean(R.styleable.SideStick_BTN_stick,true);
        setRightStick(stick_bool);

        typedArray.recycle();
    }

    void setRightStick(boolean bool){
        if(bool){
            stick.setVisibility(VISIBLE);
        }
        else{
            stick.setVisibility(INVISIBLE);
        }

    }

    public void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }
    public void setText(String text_string) {
        text.setText(text_string);
    }
    public void setText(int text_resID) {
        text.setText(text_resID);
    }
}


