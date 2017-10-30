package com.example.hp.xmoblie.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.StarItem;
import com.example.hp.xmoblie.Service.ApiClient;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017-09-20.
 */

public class BaseActivity extends Activity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
