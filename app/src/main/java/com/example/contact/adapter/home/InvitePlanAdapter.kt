package com.example.contact.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.InvitePlanItemBinding
import com.example.contact.ui.home.HomeViewModel
import com.example.contact.util.firebase.UserInfoRepository
import com.google.firebase.firestore.DocumentSnapshot
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InvitePlanAdapter(
    private val viewModel: HomeViewModel,
    private val firebaseRepository: UserInfoRepository
): RecyclerView.Adapter<InvitePlanAdapter.ViewHolder>() {
    private lateinit var binding: InvitePlanItemBinding
    private var inviteDocumentList = mutableListOf<DocumentSnapshot>()

    fun setInviteData(list: MutableList<DocumentSnapshot>){
        inviteDocumentList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(position: Int){
            binding.title.text = inviteDocumentList[position].data!!["title"].toString()

            val member = StringBuilder()
            val memberUidList = inviteDocumentList[position].data!!["member"] as ArrayList<String>
            memberUidList.forEach { uid ->
                CoroutineScope(Dispatchers.Main).launch {
                    firebaseRepository.getUserInfo(uid).asFlow<UserInfo>().collect{
                        val displayName = it!!.displayName.toString()
                        member.append("$displayName ")

                        binding.member.text = member.toString()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvitePlanAdapter.ViewHolder {
        binding = InvitePlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: InvitePlanAdapter.ViewHolder, position: Int) {
        holder.bind(position)

        binding.acceptButton.setOnClickListener {
            viewModel.inviteButton(inviteDocumentList[position].id, true)
        }

        binding.refuseButton.setOnClickListener {
            viewModel.inviteButton(inviteDocumentList[position].id, false)
        }
    }

    override fun getItemCount(): Int = inviteDocumentList.size

}