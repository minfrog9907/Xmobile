package com.example.hp.xmoblie;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by HP on 2017-09-20.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic_1.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumGothic_0.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "fonts/HPSimplified_It.ttf"))
                .addCustom2(Typekit.createFromAsset(this, "fonts/HPSimplified_Lt.ttf"))
                .addCustom3(Typekit.createFromAsset(this, "fonts/HPSimplified_LtIt.ttf"))
                .addCustom4(Typekit.createFromAsset(this, "fonts/HPSimplified_Rg.ttf"));
    }
}
