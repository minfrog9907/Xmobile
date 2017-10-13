package com.example.hp.xmoblie.Activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.xmoblie.R;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HP on 2017-09-25.
 */

public class BillsTracingActivity extends ActionBarActivity {
    Bitmap image[] = new Bitmap[5];
    String uri[];
    int cnt;
    private TessBaseAPI mTess;
    String datapath = "";
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_camera, null);
        actionBar.setCustomView(mCustomView);

        cnt=getIntent().getIntExtra("cnt",0);
        uri = getIntent().getStringArrayExtra("uri");
        for(int i=0; i<cnt;++i ){
            try {
                image[i]= MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //image = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        datapath = getFilesDir()+ "/tesseract/";

        checkFile(new File(datapath + "tessdata/"));

        String lang = "kor";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);


        Button test  = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage();
            }
        });

    }

    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/kor.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    public void processImage() {
        for (int i = 0; i < cnt; ++i) {
            String OCRresult = null;
            mTess.setImage(image[i]);
            OCRresult = mTess.getUTF8Text();
           Toast.makeText(getApplicationContext(),""+OCRresult,Toast.LENGTH_SHORT).show();
        }
    }

}