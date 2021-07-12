package com.ble.demo


import android.Manifest
import android.os.Build
import androidx.core.content.ContextCompat
import com.ble.demo.adapter.MyVPAdapter
import com.ble.demo.base.BaseActivity
import com.ble.demo.fragment.BDFragment
import com.ble.demo.fragment.IndexFragment
import com.ble.demo.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var permission = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getLlayoutId(): Int  = R.layout.activity_main

    override fun initParams() {
    }

    override fun initView() {
         hideBack()
         vp2.isUserInputEnabled = false //禁止滑动
         vp2.adapter =  MyVPAdapter(this, listOf(IndexFragment(),BDFragment(),MineFragment()))
        // vp2.adapter =  MyVPAdapter(this, listOf(BDFragment()))

        ll1.setOnClickListener {
            updateUI(0)

        }

        ll2.setOnClickListener {
            updateUI(1)

        }
        ll3.setOnClickListener {
            updateUI(2)

        }
    }
    
     fun updateUI(index:Int){
         vp2.setCurrentItem(index,false)

         iv1.setImageResource(if (index == 0 ) R.mipmap.index2 else R.mipmap.index)
         tv1.setTextColor(if ( index == 0) ContextCompat.getColor(this,R.color.red2) else  ContextCompat.getColor(this,R.color.gray2) )


         iv2.setImageResource(if (index == 1 ) R.mipmap.de2 else R.mipmap.de)
         tv2.setTextColor(if ( index == 1) ContextCompat.getColor(this,R.color.red2) else  ContextCompat.getColor(this,R.color.gray2) )

         iv3.setImageResource(if (index == 2 ) R.mipmap.main2 else R.mipmap.main)
         tv3.setTextColor(if ( index == 2) ContextCompat.getColor(this,R.color.red2) else  ContextCompat.getColor(this,R.color.gray2) )





//          if (index == 1){
//              ll_contain.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
//          }else{
//              ll_contain.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
//          }
//
//         if (index == 2){
//             setHeadColor(R.color.red2)
//         }else{
//             setHeadColor(R.color.tool_bar)
//
//         }

     }

    override fun initValue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission,1)
        }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return  Navigation.findNavController(this, R.id.host_navigation).navigateUp()
//    }



}