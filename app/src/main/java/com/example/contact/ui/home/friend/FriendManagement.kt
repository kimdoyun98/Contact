package com.example.contact.ui.home.friend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.databinding.ActivityFriendManagementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendManagement : AppCompatActivity() {
    private lateinit var binding: ActivityFriendManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.friendManagementToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addFriendButton.setOnClickListener {
            startActivity(Intent(this, AddFriend::class.java))
        }

    }
}