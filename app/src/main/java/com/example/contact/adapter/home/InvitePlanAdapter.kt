package com.example.contact.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.plan.Plan
import com.example.contact.databinding.InvitePlanItemBinding
import com.example.contact.ui.home.HomeViewModel
import com.google.firebase.firestore.DocumentSnapshot

class InvitePlanAdapter(
    private val viewModel: HomeViewModel
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
            //TODO 멤버 변수 String으로 변환해야함
            binding.member.text = inviteDocumentList[position].data!!["member"].toString()

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