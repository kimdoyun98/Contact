package com.example.contact.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.ChatAddFriendItemBinding
import com.example.contact.ui.home.add_plan.AddFriendToPlanViewModel

class FriendListAdapter(
    private val viewModel: AddFriendToPlanViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    private lateinit var binding: ChatAddFriendItemBinding
    private var uidList = ArrayList<String>()

    inner class ViewHolder(
        v: View,
        private val addFriendToPlanViewModel: AddFriendToPlanViewModel,
        private val lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            binding.viewModel = addFriendToPlanViewModel
            binding.lifecycleOwner = this@ViewHolder.lifecycleOwner

            binding.currentUid = uidList[position]
        }
    }

    fun setUidList(list: ArrayList<String>?){
        if(list != null) uidList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListAdapter.ViewHolder {
        binding = ChatAddFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, viewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: FriendListAdapter.ViewHolder, position: Int) {
        holder.bind(position)

        binding.root.setOnClickListener {
            viewModel.setOnClick(uidList[position])
        }
    }

    override fun getItemCount(): Int = uidList.size
}