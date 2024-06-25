package com.example.contact.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.ChatListItemBinding
import com.example.contact.ui.plan.PlanClickEvent
import com.google.firebase.firestore.DocumentSnapshot

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private lateinit var binding: ChatListItemBinding
    private var docList = mutableListOf<DocumentSnapshot>()
    private var onClickEvent: PlanClickEvent? = null

    fun setList(list: MutableList<DocumentSnapshot>){
        docList = list
        notifyDataSetChanged()
    }

    fun setClickEvent(listener: PlanClickEvent){
        onClickEvent = listener
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            var title = docList[position].data?.get("title") as String
            title = title.substring(1, title.length-1)
            binding.title.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = docList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        binding.root.setOnClickListener {
            onClickEvent!!.onPlanClickEvent(docList[position])

        }
    }
}