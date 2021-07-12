package com.ble.demo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.ble.mylibrary.outdevice.LScaleHelper
import com.ble.demo.R
import com.ble.demo.base.BaseActivity
import com.ble.demo.entity.EventMsg
import com.ble.demo.utils.DateUtil
import kotlinx.android.synthetic.main.activity_biao_ding.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BiaoDingActivity : BaseActivity() {
    var weightMAX = "" //量程
    var weightCOE = ""//标定值的内码----100kg的内码
    var weightREV = ""//标定的砝码值
    var weightDec = ""//分度值
    var DW = ""//单位    00:公斤 01:克  02:市斤 03:英镑
    var Dot = "" //小数位
    var Zero = ""//零点内码值
    var rADC_Delta = ""//当前内码值
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biao_ding)
    }

    override fun getLlayoutId(): Int  = R.layout.activity_biao_ding


    override fun initParams() {
              EventBus.getDefault().register(this)
    }

    var biaodingzhi = ""

    override fun initView() {

        setTitles("标定")
          rb_50.isChecked = true
        bt_sure.setOnClickListener {
            var num = 0
             if (Dot.contains("2")){
                 num = 2
             }else if (Dot.contains("3")){
                 num = 3
             }
            var order = ""
            if (biaodingzhi.length == 2){
                if (num == 2){
                    order = "BD02#0"+biaodingzhi+"00"
                }else if (num == 3){
                    order = "BD02#"+biaodingzhi+"000"
                }
            }else if ( biaodingzhi.length == 3){
                if (num == 2){
                    order = "BD02#"+biaodingzhi+"000"
                }else if (num == 3){
                   tv_rec.append("标定发生错误")
                }
            }
            sendData(order)
        }
           bt_start.setOnClickListener {
                  biaodingzhi = getBDValue()
               val coroutines = CoroutineScope(Dispatchers.Main + Job())
                coroutines.launch {
                     startBD()
                }
           }
    }

    private fun getBDValue(): String {
          var data =""
           when (rg.checkedRadioButtonId) {
            R.id.rb_30 -> {
                data = "30"
            }
            R.id.rb_50 -> {
                data = "50"
            }
            R.id.rb_60 -> {
                data = "60"
            }
        }
         return  data
    }

    @SuppressLint("SetTextI18n")
    private fun startBD() {
        GlobalScope.launch(Dispatchers.Main) {
            sendData("BD01")
            tv_rec.text = "1. ${DateUtil.getDate_ms()}--  零点检测\r\n"

        }


    }
    var isError = false
    @Subscribe
    fun   bleEvent(msg:EventMsg){
        if (msg.flag == "dis"){
            isError = true
            tv_rec.append("${DateUtil.getDate_ms()} 出现错误,连接断开\r\n")
            bt_sure.isEnabled = false
        }
    }
  private fun sendData(data:String){
      LScaleHelper.getInstance(this).sendData(data)
  }
    @SuppressLint("SetTextI18n")
    override fun initValue() {


        LScaleHelper.getInstance(this).getBDData {  data  ->
            tv_weight.text = data
            Log.i("abc",data)
            when {
                data == "ZEROFAIL" ->{
                    tv_rec.append("${DateUtil.getDate_ms()} -- 错误: 当前称台,不是零点,请拿走东西,重新标定 \r\n")
                }
                data.startsWith("weightMAX") -> {
                    tv_rec.append(" 2.${DateUtil.getDate_ms()}--  接收原始数据\r\n")
                    weightMAX = data.split("=")[1]
                    Log.i("abc","weightMAX::$weightMAX")
                }
                data.startsWith("weightCOE") -> {
                    weightCOE = data.split("=")[1]
                    Log.i("abc","weightCOE::$weightCOE")

                }
                data.startsWith("weightREV") -> {
                    weightREV = data.split("=")[1]
                    Log.i("abc","weightREV::$weightREV")

                }
                data.startsWith("weightDec") -> {
                    weightDec = data.split("=")[1]
                    Log.i("abc","weightDec::$weightDec")

                }
                data.startsWith("DW") -> {
                    DW = data.split("=")[1]
                    Log.i("abc","DW::$DW")

                }
                data.startsWith("Dot") -> {
                    Dot = data.split("=")[1]
                    Log.i("abc","Dot::$Dot")

                }
                data.startsWith("Zero") -> {
                    tv_rec.append(" 3. ${DateUtil.getDate_ms()} -- 请放${biaodingzhi} 公斤的砝码\r\n")
                    Zero = data.split("=")[1]
                    Log.i("abc","Zero::$Zero")
                    GlobalScope.launch(Dispatchers.Main) {
                        bt_sure.isEnabled = true
                    }
                }
                data.startsWith("Light_weight") -> {
                    tv_rec.append(" 砝码值太小,请重新放入砝码\r\n")
                }
                data.startsWith("rADC_Delta") -> {
                    rADC_Delta = data.split("=")[1]
                    Log.i("abc","rADC_Delta::$rADC_Delta")

                }
                data.contains("Successful") -> {
                    tv_rec.append(" 4. 标定成功\r\n")
                    GlobalScope.launch(Dispatchers.Main) {
                        bt_sure.isEnabled = false
                    }
                }
            }
        }
    }
}