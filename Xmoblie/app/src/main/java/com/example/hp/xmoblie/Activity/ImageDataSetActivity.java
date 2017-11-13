package com.example.hp.xmoblie.Activity;

import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.Items.BoundingBoxItem;
import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.OCRLineDataItem;
import com.example.hp.xmoblie.Items.OCRWordDataItem;
import com.example.hp.xmoblie.Items.OCRWordsDataItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-10-24.
 */

public class ImageDataSetActivity extends AppCompatActivity {
    ApiClient apiClient;

    int history = -1;
    int mode = 0;
    int imgWidth, imgHeight;
    int width, height;
    int btnMode[] = new int[4];

    static float xScope, yScope;

    String base64String;
    String fulldata[] = new String[4];

    int startX, startY;

    List<OCRLineDataItem> list;
    List<OCRWordsDataItem> words;
    List<OCRWordDataItem> word;

    ArrayList<BoundingBoxItem> bounding;
    ArrayList<BoundingBoxItem> throwBound;

    SideStick_BTN nameBtn;
    SideStick_BTN priceBtn;
    SideStick_BTN locationBtn;
    SideStick_BTN commitBtn;

    BoundingBoxItem startBound;
    BoundingBoxItem lastBound;

    TextView inform;

    ImageView imageView;
    ImageView selectView[] = new ImageView[4];

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
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file://" + getIntent().getStringExtra("node")));
            base64String = encodeTobase64(bm);
            width = bm.getWidth();
            height = bm.getHeight();

        } catch (IOException e) {
            e.printStackTrace();
        }

        btnMode[1] = 0;
        btnMode[2] = 0;
        btnMode[3] = 0;

        fulldata[1] = "";
        fulldata[2] = "";
        fulldata[3] = "";

        apiClient = ApiClient.service;

        bounding = new ArrayList<BoundingBoxItem>();
        throwBound = new ArrayList<BoundingBoxItem>();

        imageView = (ImageView) findViewById(R.id.imagedata_imageview);
        selectView[1] = (ImageView) findViewById(R.id.imagedataset_Name);
        selectView[2] = (ImageView) findViewById(R.id.imagedataset_Price);
        selectView[3] = (ImageView) findViewById(R.id.imagedataset_location);

        inform = (TextView) findViewById(R.id.imagedataset_inform);

        nameBtn = (SideStick_BTN) findViewById(R.id.imagedataset_NameBtn);
        priceBtn = (SideStick_BTN) findViewById(R.id.imagedataset_PriceBtn);
        locationBtn = (SideStick_BTN) findViewById(R.id.imagedataset_locationBtn);
        commitBtn = (SideStick_BTN) findViewById(R.id.imagedataset_commitBtn);

        absoluteLayout = (AbsoluteLayout) findViewById(R.id.imagedataset_absolute);

        Glide.with(this).load(getIntent().getStringExtra("node")).into(imageView);
        onWindowFocusChanged(true);

        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
            }
        });
        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrice();
            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCommit();
            }
        });

        absoluteLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mode != 0) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        setFirstTouch(x,y);

                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && startBound != null) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        setDrag(x,y);

                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        setLastTouch();
                        return false;
                    }
                }
                return true;
            }
        });

    }

    private void setFirstTouch(int x,int y){
        for (int i = 0; i < bounding.size() / 2; ++i) {
            if (bounding.get(i).crushX(x) && bounding.get(i).crushY(y)) {
                throwBound.clear();
                lastBound = null;

                history = i;
                fulldata[mode] = "";

                startBound = bounding.get(i);

                startX = startBound.getLeft() + imageView.getLeft();
                startY = startBound.getTop() + imageView.getTop();

                setSelectView(startBound.getWidth(), startBound.getHeight(), startX, startY);
                break;
            }
        }
    }

    private void setDrag(int x,int y){
        for (int i = 0; i < bounding.size() / 2; ++i) {
            if (bounding.get(i).crushX(x) && bounding.get(i).crushY(y) && i != history) {
                history = i;

                lastBound = bounding.get(i);

                startX = startBound.getLeft() < lastBound.getLeft() ? startBound.getLeft() : lastBound.getLeft();
                startY = startBound.getTop() < lastBound.getTop() ? startBound.getTop() : lastBound.getTop();

                setSelectView(calculateWH(), startX, startY);
                break;
            }
        }
    }

    private void setLastTouch(){
        if (startBound != null && lastBound == null) {
            throwBound.add(startBound);
        } else if (startBound != null && lastBound != null) {
            for (int i = 0; i < bounding.size() / 2; ++i) {
                if (bounding.get(i).insideX(sides())) {
                    throwBound.add(bounding.get(i));

                }
            }
        }
    }
    private void setName() {
        mode = 1;
        if (btnMode[mode] == 0) {
            changeBtnStyle();
        } else {
            getData();
        }
    }

    private void setPrice() {
        mode = 2;
        if (btnMode[mode] == 0) {
            changeBtnStyle();
        } else {
            getData();
        }
    }

    private void setLocation() {
        mode = 3;
        if (btnMode[mode] == 0) {
            changeBtnStyle();
        } else {
            getData();
        }
    }

    private void setCommit() {
        if (btnMode[1] == 0 && btnMode[2] == 0 && btnMode[3] == 0) {
            startActivityForResult(new Intent(ImageDataSetActivity.this, AreYouSurePopUp.class).putExtra("name", fulldata[1])
                    .putExtra("price", fulldata[2])
                    .putExtra("place", fulldata[3]), 2222);
        }

    }

    private void getData() {
        if (startBound == null && lastBound == null) {
            Toast.makeText(getApplicationContext(), "선택된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < throwBound.size(); ++i) {
                BoundingBoxItem bx = throwBound.get(i);
                fulldata[mode] += list.get(bx.getI()).getLines().get(bx.getJ()).getWords().get(bx.getZ()).getText() + " ";
            }
            startBound = null;
            lastBound = null;
            btnMode[mode] = 0;
            resetSymbol();
            throwBound.clear();
            startActivityForResult(new Intent(ImageDataSetActivity.this, EditDataPopUp.class).putExtra("data", fulldata[mode]), 1111);
        }
    }

    private void OCR() {
        if (bounding.size() == 0) {
            Log.e("OCR", "active");
            final Call<OCRDataItem> call = apiClient.repoOCR(getIntent().getStringExtra("token"), base64String);//
            call.enqueue(new Callback<OCRDataItem>() {
                @Override
                public void onResponse(Call<OCRDataItem> call,
                                       Response<OCRDataItem> response) {
                    list = response.body().getRegions();
                    for (int i = 0; i < list.size(); ++i) {
                        words = list.get(i).getLines();
                        for (int j = 0; j < words.size(); ++j) {
                            word = words.get(j).getWords();
                            for (int z = 0; z < word.size(); ++z) {
                                BoundingBoxItem bx = new BoundingBoxItem();
                                bx.setScope(xScope, yScope);
                                bx.setBounding(word.get(z).getBoundingBox(), i, j, z);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imgWidth = imageView.getWidth();
        imgHeight = imageView.getHeight();

        xScope = (float) imgWidth / (float) width;
        yScope = (float) imgHeight / (float) height;

        OCR();
    }

    public void setSelectView(int width, int height, int px, int py) {
        absoluteLayout.removeAllViews();
        absoluteLayout.addView(imageView);
        for (int i = 1; i < 4; ++i) {
            if (mode != i)
                absoluteLayout.addView(selectView[i]);
            else
                absoluteLayout.addView(selectView[mode], new AbsoluteLayout.LayoutParams(width, height, px, py));
        }
    }

    public void setSelectView(int wh[], int px, int py) {
        absoluteLayout.removeAllViews();
        absoluteLayout.addView(imageView);
        for (int i = 1; i < 4; ++i) {
            if (mode != i)
                absoluteLayout.addView(selectView[i]);
            else
                absoluteLayout.addView(selectView[mode], new AbsoluteLayout.LayoutParams(wh[0], wh[1], px, py));
        }
    }

    public int[] calculateWH() {
        int xy[] = new int[2];
        if (lastBound.getLeft() >= startBound.getLeft()) {
            xy[0] = Math.abs(lastBound.getRight() - startBound.getLeft());
        } else if (lastBound.getLeft() <= startBound.getLeft()) {
            xy[0] = Math.abs(startBound.getRight() - lastBound.getLeft());
        }
        if (lastBound.getTop() >= startBound.getTop()) {
            xy[1] = Math.abs(lastBound.getBottom() - startBound.getTop());
        } else if (lastBound.getTop() <= startBound.getTop()) {
            xy[1] = Math.abs(startBound.getBottom() - lastBound.getTop());
        }
        return xy;
    }

    public int[][] sides() {
        int xy[][] = new int[2][2];
        if (lastBound.getLeft() >= startBound.getLeft()) {
            xy[0][0] = startBound.getLeft();
            xy[0][1] = lastBound.getRight();
        } else if (lastBound.getLeft() <= startBound.getLeft()) {
            xy[0][0] = lastBound.getLeft();
            xy[0][1] = startBound.getRight();
        }
        if (lastBound.getTop() >= startBound.getTop()) {
            xy[1][0] = startBound.getTop();
            xy[1][1] = lastBound.getBottom();
        } else if (lastBound.getTop() <= startBound.getTop()) {
            xy[1][0] = lastBound.getTop();
            xy[1][1] = startBound.getBottom();
        }
        return xy;
    }

    public void changeBtnStyle() {
        if (btnMode[1] == 0 && btnMode[2] == 0 && btnMode[3] == 0) {
            switch (mode) {
                case 1:
                    inform.setText("가게 이름을 드래그 해주세요");

                    nameBtn.setSymbol(R.drawable.checked_light);
                    nameBtn.setText("확인");

                    priceBtn.setSymbol(R.drawable.price_tag);
                    priceBtn.setText("가격");

                    locationBtn.setSymbol(R.drawable.direction_signs);
                    locationBtn.setText("가게 주소");
                    break;

                case 2:
                    inform.setText("가격을 드래그 해주세요");

                    nameBtn.setSymbol(R.drawable.shop);
                    nameBtn.setText("가게 이름");

                    priceBtn.setSymbol(R.drawable.checked_light);
                    priceBtn.setText("확인");

                    locationBtn.setSymbol(R.drawable.direction_signs);
                    locationBtn.setText("가게 주소");
                    break;

                case 3:
                    inform.setText("가게 주소를 드래그 해주세요");

                    nameBtn.setSymbol(R.drawable.shop);
                    nameBtn.setText("가게 이름");

                    priceBtn.setSymbol(R.drawable.price_tag);
                    priceBtn.setText("가격");

                    locationBtn.setSymbol(R.drawable.checked_light);
                    locationBtn.setText("확인");
                    break;
            }
            btnMode[mode] = 1;
            setSelectView(0, 0, 0, 0);
        } else {
            for (int i = 1; i < 4; ++i)
                if (btnMode[i] == 1) mode = i;
        }
    }

    public void resetSymbol() {
        switch (mode) {
            case 1:
                nameBtn.setSymbol(R.drawable.shop);
                nameBtn.setText("가게 이름");
                break;
            case 2:
                priceBtn.setSymbol(R.drawable.price_tag);
                priceBtn.setText("가격");
                break;
            case 3:
                locationBtn.setSymbol(R.drawable.direction_signs);
                locationBtn.setText("가게 주소");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", resultCode + "");
        if (requestCode == 1111)
            switch (resultCode) {
                case 200:
                    fulldata[mode] = data.getStringExtra("data");
                    mode = -1;
                    break;
                case 400:
                    changeBtnStyle();
                    break;

            }
        if (requestCode == 2222)
            switch (resultCode) {
                case 200:
                    setResult(200, new Intent().putExtra("name", fulldata[1])
                            .putExtra("price", fulldata[2])
                            .putExtra("place", fulldata[3]));
                    finish();
                    break;
                case 400:
                    break;
            }

    }
}
