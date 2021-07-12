package com.ble.demo.view

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ble.demo.MainApp
import com.ble.mylibrary.outdevice.LScaleHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ble.demo.R
import com.ble.demo.utils.DensityUtils
import com.ble.demo.viewmodel.BleModel


class PopBlueSearchPop(
    var context: Context,
    var builder: Builder
) : PopupWindow() {
    val rec :RecyclerView
    val width_s = DensityUtils.getScreenSize(true)
    val height_s = DensityUtils.getScreenSize(false)
    val view:View

    lateinit var adapter: Adapter
    init {

         view = View.inflate(context, R.layout.pop_blue_search, null)
        rec = view.findViewById(R.id.rec)
        contentView = view
        isTouchable = true
        // 设置获取焦点
        isFocusable = true
        // 设置外部点击消失
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        animationStyle = R.style.popwin_anim_style
      //  softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        width = (width_s*0.8).toInt()
        height = (height_s*0.6).toInt()


        // 设置监听pop关闭

            setOnDismissListener {
                LScaleHelper.getInstance(context).stopscan()
            }


        elevation = 10f
        initRec(context,builder)
        LScaleHelper.getInstance(context).startscan(false) { device, rssi ->
           if ( !adapter.data.contains(device)){
               adapter.addData(device)
           }
        }
    }



    private fun initRec(context: Context, builder: Builder) {
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.shape_divider)!!)
        rec.addItemDecoration(dividerItemDecoration)
         adapter = Adapter()
        adapter.animationEnable = true
        rec.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->

                LScaleHelper.getInstance(context).connect(adapter.getItem(position)) { result, device ->
                    builder.selectCallBack(result,device)

            }
            dismiss()

        }

    }


    fun show_center(activity: Activity) {
        if (this.isShowing) {
            dismiss()
        }
        showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0)

        update()
    }
    fun show_center() {
        if (this.isShowing) {
            dismiss()
        }
        showAtLocation(view, Gravity.CENTER, 0, 0)
        update()
    }




    class Builder(private val context: Context) {
        internal lateinit var selectCallBack: (result:Int,device:BluetoothDevice?) -> Unit
         var canconnect = false
        var model:BleModel? = null

        fun setOnSelectCallback(selectCallBack:(result:Int,device:BluetoothDevice?) -> Unit): Builder {
            this.selectCallBack = selectCallBack
            return this
        }
        fun canConnect(canconnect:Boolean):Builder{
            this.canconnect = canconnect
            return  this

        }



        fun build(): PopBlueSearchPop {
            return PopBlueSearchPop(context, this)
        }

        fun  setModel(model: BleModel):Builder{
            this.model = model
            return  this
        }

    }
    class Adapter :BaseQuickAdapter<BluetoothDevice,BaseViewHolder>(R.layout.item_blue_search){
        override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
                val position = holder.layoutPosition
                holder.setText(R.id.tv_blue_name,"${position+1}. "+item.name)


        }

    }
}