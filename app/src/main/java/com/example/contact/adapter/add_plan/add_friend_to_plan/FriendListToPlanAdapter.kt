package com.example.contact.adapter.add_plan.add_friend_to_plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.AddFriendToPlanItemBinding
import com.example.contact.ui.home.add_plan.AddFriendToPlanChatViewModel

class FriendListToPlanAdapter(
    private val viewModel: AddFriendToPlanChatViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<FriendListToPlanAdapter.ViewHolder>() {
    private lateinit var binding: AddFriendToPlanItemBinding
    private var uidList = mutableListOf<String>()

    fun setUidList(friendList: MutableList<String>?){
        if (friendList != null) uidList = friendList
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        v: View,
        private val addFriendToPlanChatViewModel: AddFriendToPlanChatViewModel,
        private val lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            binding.viewModel = addFriendToPlanChatViewModel
            binding.lifecycleOwner = this@ViewHolder.lifecycleOwner

            binding.currentUid = uidList[position]
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.friendItem.setOnClickListener {
            viewModel.setOnClick(uidList[position])
        }

        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AddFriendToPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, viewModel, lifecycleOwner)
    }

    override fun getItemCount(): Int = uidList.size
}
