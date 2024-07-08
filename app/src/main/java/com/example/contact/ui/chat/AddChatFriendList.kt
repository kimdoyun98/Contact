package com.example.contact.ui.chat

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.adapter.chat.FriendListAdapter
import com.example.contact.databinding.ActivityAddChatFriendListBinding
import com.example.contact.ui.home.add_plan.AddFriendToPlanChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
class AddChatFriendList : AppCompatActivity() {
    private lateinit var binding: ActivityAddChatFriendListBinding
    private val viewModel: AddFriendToPlanChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChatFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.chatAddToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val friendListAdapter = FriendListAdapter(viewModel, this)
        binding.chatAddRecycler.adapter = friendListAdapter

        viewModel.friendList.observe(this){
            friendListAdapter.setUidList(it?.friend!!.keys.toCollection(ArrayList()))
        }

        viewModel.chatStatus.observe(this){
            if(it){
                intent.apply {
                    putExtra("docId", viewModel.docId)
                    setResult(RESULT_OK, intent)
                }
                finish()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_check -> viewModel.registerChat()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_check_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}