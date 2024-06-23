package com.example.contact.ui.home.friend

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.adapter.friend_manage.FriendListAdapter
import com.example.contact.adapter.friend_manage.RequestFriendAdapter
import com.example.contact.databinding.ActivityFriendManagementBinding
import com.example.contact.ui.home.friend.add.AddFriend
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendManagement : AppCompatActivity() {
    private lateinit var binding: ActivityFriendManagementBinding
    private val viewModel: FMViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.friendManagementToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fmViewModel = viewModel
        binding.lifecycleOwner = this

        /**
         * 친구 추가 버튼
         */
        binding.addFriendButton.setOnClickListener {
            startActivity(Intent(this, AddFriend::class.java))
        }

        /**
         * 친구 요청 RecyclerView
         */
        val requestAdapter = RequestFriendAdapter()
        binding.reqFriend.adapter = requestAdapter

        viewModel.reqFriend.observe(this){
            requestAdapter.setData(it)
        }

        /**
         * 친구 요청 수락&거절 버튼 클릭 이벤트
         */
        requestAdapter.setOnItemClickListener(object : RequestButton{
            override fun acceptButton(uid: String) {
                viewModel.requestButton(uid, true)
            }

            override fun refuseButton(uid: String) {
                viewModel.requestButton(uid, false)
            }
        })

        /**
         * 친구 목록
         */

        val friendListAdapter = FriendListAdapter(viewModel, this)
        binding.friendRecyclerView.adapter = friendListAdapter

        viewModel.friendList.observe(this){
            friendListAdapter.setUidList(it?.friend!!.keys.toMutableList(), this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}