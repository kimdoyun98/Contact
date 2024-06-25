package com.example.contact.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contact.R
import com.example.contact.adapter.chat.ChatAdapter
import com.example.contact.databinding.FragmentChatBinding
import com.example.contact.ui.chat.chatting.ChatViewModel
import com.example.contact.ui.chat.chatting.Chatting
import com.example.contact.ui.plan.PlanClickEvent
import com.example.contact.util.MyApplication
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Chat : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        /**
         * Toolbar
         */
        binding.chatToolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.chat_search -> {
                    startActivity(Intent(MyApplication.getInstance(), SearchChat::class.java))
                    true
                }
                R.id.chat_add -> {
                    startActivityForResult(Intent(MyApplication.getInstance(), AddChatFriendList::class.java), 1)
                    true
                }
                else -> false
            }
        }

        /**
         * Chat 목록
         */
        val chatAdapter = ChatAdapter()
        binding.chatRecycler.adapter = chatAdapter
        viewModel.getMyChat.observe(viewLifecycleOwner){
            val list = mutableListOf<DocumentSnapshot>()
            it?.documents?.forEach { document ->
                list.add(document)
            }
            chatAdapter.setList(list)
        }

        chatAdapter.setClickEvent(object : PlanClickEvent{
            override fun onPlanClickEvent(doc: DocumentSnapshot) {
                val chatIntent = Intent(MyApplication.getInstance(), Chatting::class.java)
                chatIntent.putExtra("docId", doc.id)
                startActivity(chatIntent)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            if(data != null){
                val docId = data.getStringExtra("docId")!!
                val chatIntent = Intent(MyApplication.getInstance(), Chatting::class.java)
                chatIntent.putExtra("docId", docId)
                startActivity(chatIntent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentChatBinding.inflate(layoutInflater).root
}