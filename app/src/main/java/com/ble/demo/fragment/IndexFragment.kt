package com.ble.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.ble.demo.adapter.IndexAdapter
import com.ble.demo.R
import com.ble.demo.entity.IndexEntity
import com.ble.demo.utils.DensityUtils
import com.ble.demo.view.BannerView
import kotlinx.android.synthetic.main.fragment_index.*

class IndexFragment : Fragment() {
    var banners = listOf(R.mipmap.aaa,R.mipmap.bbb,R.mipmap.ccc)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return   inflater.inflate(R.layout.fragment_index,null,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         initView()
    }

    private fun initView() {
        //这里考虑到不同手机分辨率下的情况
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(400F))
        carouselView.layoutParams = params
        carouselView.setSwitchTime(2000)
        carouselView.setAdapter(MyAdapter())
        initRec()

    }
    private fun initRec() {
        var adapter = IndexAdapter()
        rec.adapter =  adapter
        var data = arrayListOf<IndexEntity>()
        repeat(10){
            data.add(IndexEntity(R.mipmap.test,"标定"))
        }
        adapter.setList(data)
    }

    inner  class MyAdapter : BannerView.Adapter {
        override fun isEmpty(): Boolean {
            return banners.isEmpty()
        }

        override fun getView(position: Int): View {

            val imageView = ImageView(requireContext())
            imageView.setBackgroundResource(banners[position])
            //   imageView.setOnClickListener { T.showShort( "Now is $position") }
            return imageView
        }

        override fun getCount(): Int {
            return banners.size
        }
    }
}