package com.ble.demo.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.ble.demo.R
import kotlinx.android.synthetic.main.loading_progress.*

class LoadingProgress( windowContext: Context) :
    AlertDialog(windowContext, R.style.MyProgressDialogStyle) {

    var width: Int
    val view: View

    var message = "加载中"
    init {
        val widthPixels: Double = windowContext.resources.displayMetrics.widthPixels.toDouble()
        width = (widthPixels * 0.7).toInt()
        val inflater = LayoutInflater.from(windowContext)
        view = inflater.inflate(R.layout.loading_progress, null)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var params = LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT, 0f)
        setContentView(view, params)
        tv_msg.text = message
    }




    inline fun show(func: LoadingProgress.() -> Unit) = apply {
        this.func()
        this.show()

    }

    fun message(msg: String) : LoadingProgress {
        this.message = msg
        tv_msg?.text = msg
        return this
    }



}