package com.example.contact.adapter.plan

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contact.databinding.ImageBigItemBinding
import com.example.contact.util.MyApplication

class ImageViewpagerAdapter: RecyclerView.Adapter<ImageViewpagerAdapter.ViewHolder>() {
    private lateinit var binding: ImageBigItemBinding
    private var imgUriList = mutableListOf<Uri>()

    fun setData(list: List<Uri>){
        imgUriList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            Glide
                .with(MyApplication.getInstance())
                .load(imgUriList[position])
                .fitCenter()
                .into(binding.image)
            binding.size = imgUriList.size
            binding.count = "${position+1}/${imgUriList.size}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewpagerAdapter.ViewHolder {
        binding = ImageBigItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ImageViewpagerAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = imgUriList.size

}