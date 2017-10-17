package com.example.hp.xmoblie.Activity;

/**
 * Created by HP on 2017-09-27.
 */

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hp.xmoblie.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageGrayScaleActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    public LinearLayout thumnail;
    ImageView preview;
    public ImageView[] thumnailImages = new ImageView[5];
    public int cnt = 0;
    public String[] uri = new String[5];

    String node;


    ImageView imageVIewInput;
    ImageView imageVIewOuput;
    private Mat img_input;
    private Mat img_output;

    private static final String TAG = "opencv";

    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d( TAG, "copyFile :: 다음 경로로 파일복사 "+ pathDir);
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
            Log.d(TAG, "copyFile :: 파일 복사 중 예외 발생 "+e.toString() );
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        imageVIewInput = (ImageView)findViewById(R.id.imageViewInput);
        imageVIewOuput = (ImageView)findViewById(R.id.imageViewOutput);

        //이미 사용자에게 퍼미션 허가를 받음.
        read_image_file();
        imageprocess_and_showResult();

    }

    private void imageprocess_and_showResult() {

        imageprocessing(img_input.getNativeObjAddr(), img_output.getNativeObjAddr());

        Bitmap bitmapInput = Bitmap.createBitmap(img_input.cols(), img_input.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_input, bitmapInput);
        imageVIewInput.setImageBitmap(bitmapInput);

        Bitmap bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        imageVIewOuput.setImageBitmap(bitmapOutput);
    }

    private void read_image_file() {
        copyFile("Download/bills/asdfasdf.jpg");

        img_input = new Mat();
        img_output = new Mat();

        loadImage("/Download/bills/asdfasdf.jpg", img_input.getNativeObjAddr());
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void loadImage(String imageFileName, long img);
    public native void imageprocessing(long inputImage, long outputImage);
}