package com.example.contact.adapter.add_plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.ChoiceFriendListItemBinding
import com.example.contact.util.firebase.FirebaseRepository
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class CurrentFriendListParentAdapter: RecyclerView.Adapter<CurrentFriendListParentAdapter.ViewHolder>() {
    lateinit var binding: ChoiceFriendListItemBinding
    lateinit var firebaseRepository: FirebaseRepository
    var uidList = mutableListOf<String>()

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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ChoiceFriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = uidList.size
}