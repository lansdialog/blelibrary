package com.ble.demo.utils


import android.view.Gravity
import android.widget.Toast
import com.ble.demo.MainApp

/**
 * Toast统一管理类
 */

object T {
    // Toast
    private var toast: Toast? = null

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    fun showShort(message: CharSequence?) {
        if (null == toast) {
            toast =
                Toast.makeText(MainApp.context, message, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(message)
        }
        toast!!.show()
    }

    @JvmStatic
    fun showShortCenter(message: CharSequence?) {
        if (null == toast) {
            toast = Toast.makeText(MainApp.context, message, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(message)
        }
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

    fun showLong(messageRes: Int) {
        showLong(MainApp.context.getString(messageRes))
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    fun showLong(message: CharSequence?) {
        if (null == toast) {
            toast =
                Toast.makeText(MainApp.context, message, Toast.LENGTH_LONG)
        } else {
            toast!!.setText(message)
        }
        toast!!.show()
    }

    /**
     * Hide the toast, if any.
     */
    fun hideToast() {
        if (null != toast) {
            toast!!.cancel()
        }
    }
}