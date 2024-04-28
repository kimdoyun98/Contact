package com.example.contact.adapter.add_plan.add_friend_to_plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.adapter.add_plan.CurrentFriendListParentAdapter
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
    firebaseRepository: FirebaseRepository
): CurrentFriendListParentAdapter() {
    init {
        this.firebaseRepository = firebaseRepository
    }

    fun setFriendList(friendList: MutableList<String>?){
        if (friendList != null) this.uidList = friendList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.cancelButton.setOnClickListener {
            viewModel.cancel(uidList[position])
        }

        holder.bind(position)
    }
}