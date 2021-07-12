package com.ble.demo.base;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

/**
 * Created by admin on 2018/7/19.
 */

public class EditTextIntractor {

    /**
     * 点击软键盘event.getx是取不到值的
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] location = {0, 0};
            v.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
          //  Log.d("abc", "getLocationOnScreen(): left = " + location[0] + "  top=" + location[1]);
          //  Log.d("abc", "x:" + event.getX() + "/y:" + event.getY());
            if (event.getX() < left || (event.getX() > left + v.getWidth())
                    || event.getY() < top || (event.getY() > top + v.getHeight())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void addOnSoftKeyBoardVisibleListener(Activity activity, final IKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                //获得键盘高度
                int keyboardHeight = hight - displayHight;
                boolean visible = (double) displayHight / hight < 0.8;//可见高度低于屏幕高度的4/5判定为键盘出来了
                if (visible != isVisiableForLast) {
                    listener.onSoftKeyBoardVisible(visible, keyboardHeight);

                }
                isVisiableForLast = visible;

            }

        });
    }

    public interface IKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible, int windowBottom);
    }
    boolean isVisiableForLast = false;
}
