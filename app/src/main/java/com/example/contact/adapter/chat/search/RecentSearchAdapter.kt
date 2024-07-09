package com.example.contact.adapter.chat.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.ChoiceFriendListItemBinding
import com.example.contact.ui.chat.search.RecentButtonClick
import com.example.contact.ui.chat.search.SearchChatViewModel
import com.example.contact.util.MyApplication

class RecentSearchAdapter(
    private val viewModel: SearchChatViewModel
): RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {
    private lateinit var binding: ChoiceFriendListItemBinding
    private var list = ArrayList<String>()
    private var onClickEvent: RecentButtonClick? = null
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            binding.displayName = list[position]

            binding.textView8.setOnClickListener {
                onClickEvent?.onClickRecentSearch(list[position])
            }
            binding.cancelButton.setOnClickListener {
                list.remove(list[position])
                notifyItemRemoved(position)
                viewModel.deleteRecentSearch(list[position])
            }
        }
    }

    init {
        val recent = MyApplication.prefs.getRecentSearch()
        repeat(recent.length()){
            list.add(recent.optString(it))
        }
        notifyDataSetChanged()
    }

    fun setClickEvent(event: RecentButtonClick){
        onClickEvent = event
    }

    fun addRecentSearch(query: String){
        if(list.contains(query)) return
        list.add(0, query)
        notifyItemInserted(0)

        //저장
        viewModel.saveRecentSearch(query)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchAdapter.ViewHolder {
        binding = ChoiceFriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecentSearchAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}