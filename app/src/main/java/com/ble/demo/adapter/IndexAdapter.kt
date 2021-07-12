package com.ble.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ble.demo.R
import com.ble.demo.entity.IndexEntity

class IndexAdapter :BaseQuickAdapter<IndexEntity,BaseViewHolder>(R.layout.item_rec_index){
    override fun convert(holder: BaseViewHolder, item: IndexEntity) {

          holder.setImageResource(R.id.iv,item.img)
          holder.setText(R.id.title,item.title)
    }
}