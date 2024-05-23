package com.example.contact.adapter.plan.DutchPay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.DutchMemberListBinding
import com.example.contact.ui.plan.detail.info.dutchpay.DutchAddViewModel

class DutchAddMemberAdapter (
    private val viewModel: DutchAddViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<DutchAddMemberAdapter.ViewHolder>() {
    private lateinit var binding: DutchMemberListBinding
    private var memberList = ArrayList<String>()

    fun setData(list: ArrayList<String>){
        memberList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        v: View,
        private val viewModel: DutchAddViewModel,
        private val lifecycleOwner: LifecycleOwner
        ): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            binding.viewModel = viewModel
            binding.lifecycleOwner = lifecycleOwner
            binding.currentUid = memberList[position]
            binding.displayName = memberList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DutchAddMemberAdapter.ViewHolder {
        binding = DutchMemberListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, viewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: DutchAddMemberAdapter.ViewHolder, position: Int) {
        holder.bind(position)
        binding.friendItem.setOnClickListener {
            viewModel.setOnClick(memberList[position])
        }
    }

    override fun getItemCount(): Int = memberList.size
}
