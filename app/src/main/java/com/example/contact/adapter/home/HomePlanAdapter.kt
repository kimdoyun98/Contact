package com.example.contact.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.HomeMyplanItemBinding
import com.example.contact.ui.home.HomeViewModel

class HomePlanAdapter(
    private val viewModel:HomeViewModel
): RecyclerView.Adapter<HomePlanAdapter.ViewHolder>() {
    private lateinit var binding: HomeMyplanItemBinding
    private var planList = mutableListOf<Pair<String, String>>()

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            binding.num.text = "${position+1}."
            binding.title.text = planList[position].first
            binding.dDay.text = viewModel.getDDay(planList[position].second)
        }
    }

    fun setPlanList(list: MutableList<Pair<String, String>>){
        planList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePlanAdapter.ViewHolder {
        binding = HomeMyplanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HomePlanAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = planList.size
}