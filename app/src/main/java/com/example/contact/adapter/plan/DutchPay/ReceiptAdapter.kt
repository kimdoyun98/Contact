package com.example.contact.adapter.plan.DutchPay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.plan.dutch.ReceiptData
import com.example.contact.databinding.ReceiptItemBinding

class ReceiptAdapter (
    private val data: ArrayList<ReceiptData>
): RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {
    private lateinit var binding: ReceiptItemBinding
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            binding.data = data[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptAdapter.ViewHolder {
        binding = ReceiptItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReceiptAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = data.size
}