package com.example.contact.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.data.chat.ChattingData
import com.example.contact.databinding.MessageItemBinding
import com.google.firebase.firestore.DocumentSnapshot

class ChattingAdapter: RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {
    private lateinit var binding: MessageItemBinding
    private var docList = listOf<DocumentSnapshot>()
    private var startStatus = false

    fun setDocList(list: List<DocumentSnapshot>){
        //TODO 데이터 꼬임 고쳐야 함
        docList = list
        if(!startStatus) {
            notifyDataSetChanged()
            startStatus = true
        }
        else notifyItemInserted(list.size)
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun bind(position: Int){
            binding.chattingData =
                ChattingData(
                    date = docList[position].id,
                    message = docList[position].data!!["message"] as String,
                    author = docList[position].data!!["author"] as HashMap<String, String>
                )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingAdapter.ViewHolder {
        binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ChattingAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = docList.size


}