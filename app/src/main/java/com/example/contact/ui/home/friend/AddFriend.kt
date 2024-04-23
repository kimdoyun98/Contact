package com.example.contact.ui.home.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.contact.R
import com.example.contact.databinding.ActivityAddFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriend : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendBinding
    private val viewModel: AddFriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.addFriendToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addFriendViewModel = viewModel
        binding.lifecycleOwner = this
        binding.activity = this@AddFriend

        binding.friendSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {return false}

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getFriendSearch(query)
                return false
            }

        })
    }
}