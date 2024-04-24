package com.example.contact.ui.home.friend

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.adapter.RequestFriendAdapter
import com.example.contact.databinding.ActivityFriendManagementBinding
import com.example.contact.ui.home.friend.add.AddFriend
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FriendManagement : AppCompatActivity() {
    private lateinit var binding: ActivityFriendManagementBinding
    private val viewModel: FMViewModel by viewModels()
    @Inject lateinit var fireStore: FirebaseFirestore
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}