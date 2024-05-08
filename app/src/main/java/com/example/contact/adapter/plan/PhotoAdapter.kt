package com.example.contact.adapter.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contact.databinding.AddPhotoItemBinding
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository

open class PhotoAdapter: RecyclerView.Adapter<PhotoAdapter.ViewHolder>(){
    var imgUrl = mutableListOf<String>()
    lateinit var binding: AddPhotoItemBinding

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            if(position == imgUrl.size){
                binding.photo.visibility = View.GONE
                binding.add.visibility = View.VISIBLE
            }
            else{
                binding.photo.visibility = View.VISIBLE
                binding.add.visibility = View.GONE

                Glide.with(MyApplication.getInstance())
                    .load(imgUrl[position])
                    .centerCrop()
                    .into(binding.photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AddPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = imgUrl.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}