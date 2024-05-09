package com.example.contact.adapter.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contact.data.plan.DetailPlan
import com.example.contact.databinding.DetailPlanItemBinding
import com.example.contact.ui.plan.detail.PlanDetailClick
import com.example.contact.util.MyApplication
import com.google.firebase.firestore.DocumentSnapshot

class DetailPlanAdapter (
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<DetailPlanAdapter.ViewHolder>() {
    private lateinit var binding: DetailPlanItemBinding
    private var onClick: PlanDetailClick? = null
    private var detailPlanList = mutableListOf<DocumentSnapshot>()

    fun setOnClick(event: PlanDetailClick){
        onClick = event
    }

    fun setData(list: MutableList<DocumentSnapshot>){
        detailPlanList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        v: View,
        private val lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            val uriList = detailPlanList[position].data!!["images"]!! as ArrayList<String>

            binding.lifecycleOwner = this@ViewHolder.lifecycleOwner
            binding.planDetail = DetailPlan(
                time = detailPlanList[position].data!!["time"]!!.toString(),
                location = detailPlanList[position].data!!["location"]!!.toString(),
                images = uriList
            )

            if(uriList.isNotEmpty()){
                Glide
                    .with(MyApplication.getInstance())
                    .load(uriList[0])
                    .centerCrop()
                    .into(binding.image)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailPlanAdapter.ViewHolder {
        binding = DetailPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: DetailPlanAdapter.ViewHolder, position: Int) {
        holder.bind(position)
        binding.layout.setOnClickListener {
            val imgUri = detailPlanList[position].data!!["imgUri"]!! as ArrayList<String>
            onClick?.planDetailClick(imgUri[0])
        }
    }

    override fun getItemCount(): Int = detailPlanList.size

}