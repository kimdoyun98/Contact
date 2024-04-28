package com.example.contact.adapter.friend_manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.ReqFriendItemBinding
import com.example.contact.ui.home.friend.RequestButton
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestFriendAdapter: RecyclerView.Adapter<RequestFriendAdapter.ViewHolder>() {
    private lateinit var binding: ReqFriendItemBinding
    private lateinit var uidList: MutableList<String>

    private var itemClickListener: RequestButton? = null

    // OnItemClickListener 전달 메소드
    fun setOnItemClickListener(listener: RequestButton?) {
        itemClickListener = listener
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        init {
            binding.acceptButton.setOnClickListener {
                itemClickListener?.acceptButton(uidList[adapterPosition])
                binding.clickStatus = true
            }

            binding.refuseButton.setOnClickListener {
                itemClickListener?.refuseButton(uidList[adapterPosition])
                binding.clickStatus = false
            }
        }

        fun bind(position: Int){
            CoroutineScope(Dispatchers.Main).launch {
                FirebaseFirestore.getInstance().collection("Users").document(uidList[position])
                    .asFlow<UserInfo>().collect{
                        binding.displayName = it?.displayName
                    }
            }

            binding.uid = uidList[position]
        }
    }

    fun setData(list: MutableList<String>){
        uidList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ReqFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = uidList.size
}