package com.example.hp.xmoblie.Activity;

/**
 * Created by HP on 2017-09-27.
 */

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.xmoblie.Custom.SideStick_BTN;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.OCRLineDataItem;
import com.example.hp.xmoblie.Items.OCRWordDataItem;
import com.example.hp.xmoblie.Items.OCRWordsDataItem;
import com.example.hp.xmoblie.R;
import com.example.hp.xmoblie.Service.ApiClient;
import com.yalantis.ucrop.UCrop;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraResultActivity extends AppCompatActivity {
    int IMAGE_DATA = 1050;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }
    ImageView preview;

    String node;
    private Mat img_input;
    private Mat img_output;
    Bitmap bitmapOutput;

    private static final String TAG = "opencv";

    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d(TAG, "copyFile :: 다음 경로로 파일복사 " + pathDir);
            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            Log.d(TAG, "copyFile :: 파일 복사 중 예외 발생 " + e.toString());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        ActionBar actionBar = getSupportActionBar();

        preview = (ImageView) findViewById(R.id.cameraResult_Image);

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_camera, null);
        actionBar.setCustomView(mCustomView);


        //이미 사용자에게 퍼미션 허가를 받음.
        read_image_file();
        imageprocess_and_showResult();

        SideStick_BTN edit = (SideStick_BTN) findViewById(R.id.cameraResult_ChangeNode);
        LinearLayout share = (LinearLayout) findViewById(R.id.cameraResult_Share);
        LinearLayout upload = (LinearLayout) findViewById(R.id.cameraResult_Upload);
        LinearLayout tagEdit = (LinearLayout) findViewById(R.id.cameraResult_TagEdit);
        LinearLayout nameEdit = (LinearLayout) findViewById(R.id.cameraResult_NameEdit);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CameraResultActivity.this,ImageDataSetActivity.class)
                        .putExtra("token",getIntent().getStringExtra("token"))
                        .putExtra("node" , node),IMAGE_DATA);
            }
        });
        //Log.e("dir",getIntent().getStringExtra("node")+"   "+getIntent().getStringExtra("dir")+"/tmp/");

        LinearLayout backBtn = (LinearLayout) mCustomView.findViewById(R.id.action_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void imageprocess_and_showResult() {

        imageprocessing(img_input.getNativeObjAddr(), img_output.getNativeObjAddr());

        bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        saveImage(bitmapOutput, String.format("%d.png   ", System.currentTimeMillis()));

        preview.setImageBitmap(bitmapOutput);
    }

    private void read_image_file() {
        node = getIntent().getStringExtra("node");
        //copyFile(node);
        copyFile("/Download/bills/asdfasdf.jpg");
        img_input = new Mat();
        img_output = new Mat();

        //loadImage(node, img_input.getNativeObjAddr());
        loadImage("/Download/bills/asdfasdf.jpg", img_input.getNativeObjAddr());
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString() + "/cropedbills/";
        File myDir = new File(root);
        Log.e("node", root + image_name);
        myDir.mkdirs();
        String fname = image_name;
        node = root + image_name;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            refreshGallery(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void loadImage(String imageFileName, long img);

    public native void imageprocessing(long inputImage, long outputImage);
}