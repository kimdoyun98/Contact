package com.example.contact.adapter.plan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.PlanListItemBinding
import com.example.contact.ui.plan.PlanClickEvent
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanListAdapter constructor(
    private val firebaseRepository: FirebaseRepository
): RecyclerView.Adapter<PlanListAdapter.ViewHolder>() {
    private lateinit var binding: PlanListItemBinding
    private var planList = mutableListOf<DocumentSnapshot>()
    private var onClickEvent: PlanClickEvent? = null

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(position: Int){
            binding.title.text = planList[position].data!!["title"].toString()

            val memberUidList = planList[position].data!!["member"] as ArrayList<String>

            val member = StringBuilder()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PlanListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    fun setPlanList(list: MutableList<DocumentSnapshot>){
        planList = list
        notifyDataSetChanged()
    }

    fun setClickEvent(listener: PlanClickEvent){
        onClickEvent = listener
    }

    override fun getItemCount(): Int = planList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        binding.root.setOnClickListener {
            onClickEvent!!.onPlanClickEvent(planList[position].id)
        }
    }
}