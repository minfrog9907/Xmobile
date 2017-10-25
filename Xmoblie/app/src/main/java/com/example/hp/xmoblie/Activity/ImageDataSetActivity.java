package com.example.hp.xmoblie.Activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.Items.BoundingBoxItem;
import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.OCRLineDataItem;
import com.example.hp.xmoblie.Items.OCRWordDataItem;
import com.example.hp.xmoblie.Items.OCRWordsDataItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;

import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-24.
 */

public class ImageDataSetActivity extends AppCompatActivity {
    private static final String TAG = "opencv";

    ApiClient apiClient;

    private Mat img_input;
    private Mat img_output;

    int mode=0;
    int width,height;

    float xScope, yScope;

    String node;
    String base64String;

    int startX,startY,nowX,nowY;

    List<OCRLineDataItem> list;
    List<OCRWordsDataItem> words;
    List<OCRWordDataItem> word;
    ArrayList<BoundingBoxItem> bounding;

    SideStick_BTN nameBtn;
    SideStick_BTN priceBtn;
    SideStick_BTN locationBtn;

    TextView inform;

    ImageView imageView;
    ImageView selectView[]= new ImageView[4];

    AbsoluteLayout absoluteLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedataset);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_camera, null);
        actionBar.setCustomView(mCustomView);

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file://"+getIntent().getStringExtra("node")));
            base64String = encodeTobase64(bm);
            width = bm.getWidth();
            height = bm.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }


        apiClient = ApiClient.service;
        bounding = new ArrayList<BoundingBoxItem>();

        OCR();

        imageView  = (ImageView) findViewById(R.id.imagedata_imageview);
        selectView[1] = (ImageView)findViewById(R.id.imagedataset_Name);
        selectView[2] = (ImageView)findViewById(R.id.imagedataset_Price);
        selectView[3]  =(ImageView)findViewById(R.id.imagedataset_location);

        inform = (TextView)findViewById(R.id.imagedataset_inform);

        nameBtn=(SideStick_BTN)findViewById(R.id.imagedataset_NameBtn);
        priceBtn=(SideStick_BTN)findViewById(R.id.imagedataset_PriceBtn);
        locationBtn=(SideStick_BTN)findViewById(R.id.imagedataset_locationBtn);

        absoluteLayout = (AbsoluteLayout)findViewById(R.id.imagedataset_absolute);

        Glide.with(this).load(getIntent().getStringExtra("node")).into(imageView);

        xScope = (imageView.getRight() - imageView.getLeft())/width;
        yScope = (imageView.getBottom() - imageView.getTop())/height;

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(mode!=0) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        int x = (int)motionEvent.getX()- imageView.getLeft();
                        int y= (int)motionEvent.getY()-imageView.getTop();
                        Log.e("point",x+" "+y);
                        Log.e("size",bounding.size()+"");
                        for(int i =0; i < bounding.size(); ++i ){
                            if(bounding.get(i).crushX(x)&&bounding.get(i).crushY(y)){
                                BoundingBoxItem bx = bounding.get(i);
                                startX =bx.getLeft()+imageView.getLeft();
                                startY = bx.getTop()+imageView.getTop();
                                absoluteLayout.removeAllViews();
                                absoluteLayout.addView(imageView);
                                absoluteLayout.addView(selectView[mode],new AbsoluteLayout.LayoutParams(bx.getWidth(),bx.getHeight(),startX,startY));
                                Log.e("selected Text",list.get(bx.getI()).getLines().get(bx.getJ()).getWords().get(bx.getZ()).getText());
                                break;
                            }
                        }

                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    }
                }
                return false;
            }
        });

        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode=1;
                setName();
            }
        });
        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode=2;
                setPrice();
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode=3;
                setLocation();
            }
        });

    }
    private void setName(){
        inform.setText("가게 이름을 드래그 해주세요");
    }
    private void setPrice(){
        inform.setText("가격을 드래그 해주세요");
    }
    private void setLocation(){
        inform.setText("가게 주소를 드래그 해주세요");
    }
    private void OCR(){
        final Call<OCRDataItem> call = apiClient.repoOCR(getIntent().getStringExtra("token"), base64String);
        call.enqueue(new Callback<OCRDataItem>() {
            @Override
            public void onResponse(Call<OCRDataItem> call,
                                   Response<OCRDataItem> response) {
               list = response.body().getRegions();
                for(int i =0; i<list.size(); ++i){
                   words = list.get(i).getLines();
                    for(int j=0; j<words.size(); ++j){
                        word = words.get(j).getWords();
                        for(int z=0; z<word.size(); ++z){
                            BoundingBoxItem bx = new BoundingBoxItem();
                            bx.setBounding(word.get(z).getBoundingBox(),i,j,z);
                            bounding.add(bx);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OCRDataItem> call, Throwable t) {

            }
        });
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
