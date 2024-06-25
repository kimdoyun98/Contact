package com.example.contact.ui.chat.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.contact.R
import com.example.contact.databinding.ActivityChattingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Chatting : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    private val viewModel: ChattingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Chat Doc Id init
        viewModel.setDocID(intent.getStringExtra("docId")!!)

        //toolbar
        setSupportActionBar(binding.chattingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = viewModel.getChatInfo().title



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

        viewModel.getChatMessage()?.observe(this){

        }

    }
}