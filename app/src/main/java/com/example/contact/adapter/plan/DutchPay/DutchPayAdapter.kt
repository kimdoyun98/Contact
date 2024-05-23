package com.example.contact.adapter.plan.DutchPay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.MemberDutchItemBinding

class DutchPayAdapter: RecyclerView.Adapter<DutchPayAdapter.ViewHolder>() {
    private lateinit var binding: MemberDutchItemBinding

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun bind(position: Int){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DutchPayAdapter.ViewHolder {
        binding = MemberDutchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: DutchPayAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 0
}