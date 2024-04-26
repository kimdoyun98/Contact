package com.example.contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.AddFriendToPlanItemBinding
import com.example.contact.ui.home.add_plan.AddFriendToPlanViewModel
import com.example.contact.util.firebase.FirebaseRepository
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendListToPlanAdapter(
    private val viewModel: AddFriendToPlanViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val firebaseRepository: FirebaseRepository
): RecyclerView.Adapter<FriendListToPlanAdapter.ViewHolder>() {
    private lateinit var binding: AddFriendToPlanItemBinding
    private var uidList = mutableListOf<String>()

    fun setUidList(friendList: MutableList<String>?){
        if (friendList != null) uidList = friendList
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        v: View,
        private val addFriendToPlanViewModel: AddFriendToPlanViewModel,
        private val lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            binding.viewModel = addFriendToPlanViewModel
            binding.lifecycleOwner = this@ViewHolder.lifecycleOwner

            binding.currentUid = uidList[position]

            CoroutineScope(Dispatchers.Main).launch {
                firebaseRepository.getUserInfo(uidList[position])
                    .asFlow<UserInfo>().collect{
                        binding.displayName = it?.displayName
                    }
            }
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
