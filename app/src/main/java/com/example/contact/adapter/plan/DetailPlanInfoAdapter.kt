package com.example.contact.adapter.plan

import android.net.Uri
import com.example.contact.ui.plan.detail.info.ImageClickEvent

class DetailPlanInfoAdapter: PhotoAdapter() {
    private var clickListener: ImageClickEvent? = null

    fun setClickEvent(event: ImageClickEvent){
        clickListener = event
    }

    fun setData(list: List<Uri>){
        if(list.isNotEmpty()){
            this.imgUrl = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        binding.photo.setOnClickListener {
            // 이미지 클릭
            clickListener?.photoClick(position)
        }

        binding.add.setOnClickListener {
            // 이미지 추가
            clickListener?.addClick()
        }
    }
}