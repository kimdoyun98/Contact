package com.example.contact.ui.chat.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.contact.R
import com.example.contact.adapter.chat.ChattingAdapter
import com.example.contact.databinding.ActivityChattingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Chatting : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    private val viewModel: ChattingViewModel by viewModels()
    private lateinit var docId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Chat Doc Id init
        docId = intent.getStringExtra("docId")!!
        Log.e("docId", docId)
        viewModel.setDocID(docId)

        //toolbar
        setSupportActionBar(binding.chattingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lifecycleScope.launch {
            viewModel.getChatInfo(docId).collect{
                val title = it?.title
                supportActionBar?.title = title?.substring(1, title.length-1)
            }
        }


        binding.chattingToolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.chatting_search ->{

                    true
                }
                R.id.chatting_more -> {

                    true
                }
                else -> false
            }
        }

        /**
         * Message
         */
        val adapter = ChattingAdapter()
        binding.messageRecycler.adapter = adapter

        viewModel.getChatMessage(docId).observe(this){
            val docList = it?.documents?.sortedBy { doc -> doc.id }
            docList?.let { it1 -> adapter.setDocList(it1) }
        }


        binding.register.setOnClickListener {
            val message = binding.edit.text.toString()

            viewModel.setChatMessage(
                doc = docId,
                message = message
            )
        }
    }
}