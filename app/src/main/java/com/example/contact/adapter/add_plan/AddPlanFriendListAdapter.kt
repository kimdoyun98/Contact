package com.example.contact.adapter.add_plan

import com.example.contact.ui.home.add_plan.AddPlanViewModel
import com.example.contact.util.firebase.UserInfoRepository

class AddPlanFriendListAdapter(
    private val viewModel: AddPlanViewModel,
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