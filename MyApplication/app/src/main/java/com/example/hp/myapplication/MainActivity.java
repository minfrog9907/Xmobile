package com.example.hp.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Uri uri, uri2;
    TextView tv;
    private TessBaseAPI mTess;
    String datapath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imv = (ImageView) findViewById(R.id.imv);
        Button btn = (Button) findViewById(R.id.part);
        tv = (TextView) findViewById(R.id.asd);


        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/test");
        dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());

        uri = Uri.fromFile(new File(sdCard.getAbsolutePath() + "/Download/bills/asdfasdf.jpg"));
        uri2 = Uri.fromFile(new File(dir + "/" + fileName));

        Log.e("loc", uri + "\n" + uri2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UCrop.Options options = new UCrop.Options();
                options.setToolbarTitle("영수증 편집");
                options.setStatusBarColor(Color.parseColor("#1b1b1b"));
                options.setToolbarColor(Color.parseColor("#1f1f1f"));
                options.setActiveWidgetColor(Color.parseColor("#73F1E2"));
                options.setDimmedLayerColor(Color.parseColor("#262626"));
                options.setRootViewBackgroundColor(Color.parseColor("#1b1b1b"));
                options.setToolbarWidgetColor(Color.parseColor("#73F1E2"));
                options.setFreeStyleCropEnabled(true);
                options.useSourceImageAspectRatio();
                options.setLogoColor(Color.parseColor("#73F1E2"));

                UCrop.of(uri, uri2)
                        .withOptions(options)
                        .start(MainActivity.this);
            }
        });

        datapath = getFilesDir() + "/tesseract/";

        checkFile(new File(datapath + "tessdata/"));

        String lang = "kor";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);


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
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    public void processImage(Uri inputUri) throws IOException {

        String OCRresult = null;
        Bitmap biit = getGrayImage(MediaStore.Images.Media.getBitmap(getContentResolver(), inputUri));
        mTess.setImage(biit);
        OCRresult = mTess.getUTF8Text();
        tv.setText(OCRresult + "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == RESULT_OK)) {
            final Uri resultUri = UCrop.getOutput(data);
            try {
                processImage(resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getGrayImage(Bitmap bmpOriginal) {

        int width, height;

        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();

        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);


        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayscale;

    }


}