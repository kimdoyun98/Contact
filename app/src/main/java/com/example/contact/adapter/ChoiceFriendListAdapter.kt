package com.example.contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.ChoiceFriendListItemBinding
import com.example.contact.ui.home.add_plan.AddFriendToPlanViewModel
import com.example.contact.util.firebase.FirebaseRepository
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChoiceFriendListAdapter(
    private val viewModel: AddFriendToPlanViewModel,
    private val firebaseRepository: FirebaseRepository
): RecyclerView.Adapter<ChoiceFriendListAdapter.ViewHolder>() {
    private lateinit var binding:ChoiceFriendListItemBinding
    private var uidList = mutableListOf<String>()

    fun setUidList(friendList: MutableList<String>?){
        if (friendList != null) uidList = friendList
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            CoroutineScope(Dispatchers.Main).launch {
                firebaseRepository.getUserInfo(uidList[position])
                    .asFlow<UserInfo>().collect{
                        binding.displayName = it?.displayName
                    }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.cancelButton.setOnClickListener {
            viewModel.cancel(uidList[position])
        }

        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ChoiceFriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = uidList.size
}