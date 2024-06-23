package com.example.contact.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.databinding.FragmentChatBinding
import com.example.contact.util.MyApplication

class Chat : Fragment() {
    private lateinit var binding: FragmentChatBinding

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