package com.ble.demo.viewmodel

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.ble.mylibrary.interfaces.QModeCallback
import com.ble.mylibrary.interfaces.WeightMode
import com.ble.mylibrary.outdevice.LScaleHelper
import com.ble.demo.MainApp
import com.ble.demo.activity.BiaoDingActivity
import com.ble.demo.utils.T
import com.ble.demo.utils.T.showShortCenter
import com.ble.mylibrary.interfaces.PowerLevel
import com.ble.mylibrary.interfaces.SleepLevel
import kotlin.math.log

class BleModel : ViewModel() {
    var rssi = -1
    var voltage = -1.0
    fun zero() {
        val zero = LScaleHelper.getInstance(MainApp.context).zero()
        if (zero == -1){
            showShortCenter("当前版本没有返回值")
        } else if (zero == 0){
            showShortCenter("设备未连接")
        }else if (zero == 1){
            showShortCenter("置零成功")
        }else if (zero == 2){
            showShortCenter("有皮重不允许置零")
        }else if (zero == 3){
            showShortCenter("置零超出范围")
        }else if(zero == 0xff){
            showShortCenter("超时")
        }

    }


    fun tare() {
        val tare = LScaleHelper.getInstance(MainApp.context).tare()
        if (tare == -1){
            showShortCenter("当前版本没有返回值")
        } else if (tare == 0){
            showShortCenter("设备未连接")
        }else if (tare == 1){
            showShortCenter("去皮成功")
        }else if (tare == 2){
            showShortCenter("零点错误报警")
        }else if (tare == 3){
            showShortCenter("超过去皮范围")
        }else if (tare == 4){
            showShortCenter("有数字皮,不可去称重皮")
        }else if (tare == 5){
            showShortCenter("重量不稳定")
        }
        else if(tare == 0xff){
            showShortCenter("超时")
        }
    }
    fun set_peel(view: View) {
        MaterialDialog(view.context).show {
            title(text = "设置去皮量")
            input(hint = "请输入", inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL) { _, text ->
                val zp = LScaleHelper.getInstance(MainApp.context).setPeel(text.toString())
                 if (zp == 0){
                    showShortCenter("设备未连接")
                }else if (zp == 1){
                    showShortCenter("成功")
                }else if (zp == 2){
                    showShortCenter("有称重皮存在,添加数字皮失败")
                }else if (zp == 3){
                    showShortCenter("重量不稳定")
                }else if (zp == 4){
                    showShortCenter("零点错误")
                }else if (zp == 5){
                     showShortCenter("去皮超范围")
                 } else if(zp == 0xff){
                    showShortCenter("超时")
                }else if (zp == 0xee){
                     showShortCenter("旧版本没有成功状态返回")
                 }
            }

        }

    }
    
    fun clearTare() {
        val clear = LScaleHelper.getInstance(MainApp.context).clearTare()
        if (clear == -2){
            showShortCenter("当前版本不支持此功能")
        }else if (clear == 0){
            showShortCenter("设备没连接")
        }else if (clear == 1){
            showShortCenter("清皮成功")
        }else if (clear == 0xff){
            showShortCenter("超时")
        }
    }



    fun resetName(v: View) {
        MaterialDialog(v.context).show {
            title(text = "写蓝牙")
            input(hint = "请输入") { _, text ->
                val reset = LScaleHelper.getInstance(MainApp.context).resetName(text.toString())
                if (reset == -2){
                    showShortCenter("当前版本不支持此功能")
                }else if (reset == 0){
                    showShortCenter("设备没连接")
                }else if (reset == 1){
                    showShortCenter("重置名称成功")
                }else if (reset == 2){
                    showShortCenter("位数必须8位")
                } else if(reset == 0xff){
                    showShortCenter("超时")
                }
            }

        }

    }

    fun setWeightMode(mode: WeightMode?) {
        val b = LScaleHelper.getInstance(MainApp.context).setWeightMode(mode)
        if (b == -2){
            showShortCenter("当前版本不支持此功能")
        }else if (b == 0){
            showShortCenter("设备没连接")
        }else if (b == 1){
            showShortCenter("切换成功")
        } else if(b == 0xff){
            showShortCenter("超时")
        }
    }
    fun setPower(){
        var b = LScaleHelper.getInstance(MainApp.context).setPowerLevel(PowerLevel.LEVEL3)
        if (b == -2){
            showShortCenter("当前版本不支持此功能")
        }else if (b == 0){
            showShortCenter("设备没连接")
        }else if (b == 1){
            showShortCenter("设置成功")
        } else if(b == 0xff){
            showShortCenter("超时")
        }


    }
    fun setSleep(){
        var b = LScaleHelper.getInstance(MainApp.context).setSleepLevel(SleepLevel.LEVEL3)
        if (b == -2){
            showShortCenter("当前版本不支持此功能")
        }else if (b == 0){
            showShortCenter("设备没连接")
        }else if (b == 1){
            showShortCenter("设置成功")
        } else if(b == 0xff){
            showShortCenter("超时")
        }
    }

  fun requestVesion(){
      var version = LScaleHelper.getInstance(MainApp.context).requestVersion()
      showShortCenter("当前版本号为::$version")
  }
    fun getWeightMode() {
        var b = LScaleHelper.getInstance(MainApp.context).queryWeightMode()
        if (b == -2){
            showShortCenter("当前版本不支持此功能")
        }else if (b == 0){
            showShortCenter("设备没连接")
        }else if (b == 1){
            Toast.makeText(MainApp.context, "一般模式(单模式)", Toast.LENGTH_SHORT).show()
        } else if (b == 2){
            Toast.makeText(MainApp.context, "一般模式(双模式)", Toast.LENGTH_SHORT).show()
        }  else if (b == 3){
            Toast.makeText(MainApp.context, "动物模式(单模式)", Toast.LENGTH_SHORT).show()
        } else if (b == 4){
            Toast.makeText(MainApp.context, "动物模式(双模式)", Toast.LENGTH_SHORT).show()
        } else if(b == 0xff){
            showShortCenter("超时")
        }
    }

    fun  biaoding(view : View){
     var device =   LScaleHelper.getInstance(MainApp.context).connectDevice
        if (device != null){
            view.context.startActivity(Intent(view.context,
                BiaoDingActivity::class.java))
        }else{
            T.showLong("请先连接设备")
        }
    }


    fun closeDevice(){
        LScaleHelper.getInstance(MainApp.context).close()
    }
}