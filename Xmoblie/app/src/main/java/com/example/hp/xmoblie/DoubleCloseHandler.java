package com.example.hp.xmoblie;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by HP on 2017-09-19.
 */

public class DoubleCloseHandler {
    private long pressedTime = 0;
    private Toast toast;
    private Activity activity;

    public DoubleCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > pressedTime + 2000) {
            pressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= pressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
