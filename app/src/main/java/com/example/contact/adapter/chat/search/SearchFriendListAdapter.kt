package com.example.contact.adapter.chat.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.ChatSearchFriendItemBinding

class SearchFriendListAdapter: RecyclerView.Adapter<SearchFriendListAdapter.ViewHolder>() {
    private var list: List<Pair<String, String>> = mutableListOf()
    private lateinit var binding: ChatSearchFriendItemBinding

    fun setData(data: List<Pair<String, String>>){
        list = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            binding.displayName.text = list[position].second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendListAdapter.ViewHolder {
        binding = ChatSearchFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchFriendListAdapter.ViewHolder, position: Int) {
        holder.bind(position)
        binding.root.setOnClickListener {
            //TODO Click Event

        }
    }

    override fun getItemCount(): Int = list.size
}