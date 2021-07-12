package com.ble.demo.utils;

import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ble.demo.MainApp;

import static android.content.Context.WINDOW_SERVICE;

public class DensityUtils {



    public  static int getScreenSize(boolean isWidth) {
        WindowManager windowManager = (WindowManager) MainApp.context.getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        if (isWidth) {
            return metrics.widthPixels;
        } else {
            return metrics.heightPixels;
        }
    }

    public static int dip2px(float dp) {
        float density =  MainApp.context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 加0.5可以四舍五入
        return px;
    }

    public static float sp2px( float spValue) {
        float fontScale = MainApp.context.getResources().getDisplayMetrics().scaledDensity;
        return  (spValue * fontScale + 0.5f);
    }

}
