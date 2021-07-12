package com.ble.demo.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ble.demo.MainActivity
import com.ble.demo.R

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
         setContentView(R.layout.activity_splash)
        initView()
    }



      var count = 3
     fun initView() {

        val mHandler: Handler =
            object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {

                    if (count-- == 0){
                          removeCallbacksAndMessages(null)
                        startActivity(Intent(this@SplashActivity,
                            MainActivity::class.java))
                          finish()
                    }else{
                       sendEmptyMessageDelayed(3,1000)
                    }

                }
            }
      mHandler.sendEmptyMessage(3)
    }



}