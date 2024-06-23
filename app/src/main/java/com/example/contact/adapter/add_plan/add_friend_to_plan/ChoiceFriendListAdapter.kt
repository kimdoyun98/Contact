package com.example.contact.adapter.add_plan.add_friend_to_plan

import com.example.contact.adapter.add_plan.CurrentFriendListParentAdapter
import com.example.contact.ui.home.add_plan.AddFriendToPlanChatViewModel
import com.example.contact.util.firebase.UserInfoRepository

class ChoiceFriendListAdapter(
    private val viewModel: AddFriendToPlanChatViewModel,
    firebaseRepository: UserInfoRepository
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