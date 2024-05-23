package com.example.contact.adapter.plan.DutchPay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.NotAutoItemBinding
import com.example.contact.ui.plan.detail.info.dutchpay.DutchAddViewModel

class ManualDutchAdapter (
    private val viewModel: DutchAddViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ManualDutchAdapter.ViewHolder>() {
    lateinit var binding: NotAutoItemBinding
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
            binding.displayName = memberList[position]

            binding.notAutoEdit.addTextChangedListener {
                viewModel.map[memberList[position]] = it.toString().toInt()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManualDutchAdapter.ViewHolder {
        binding = NotAutoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, viewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: ManualDutchAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = memberList.size
}
