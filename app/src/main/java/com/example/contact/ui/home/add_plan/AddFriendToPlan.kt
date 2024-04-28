package com.example.contact.ui.home.add_plan

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.contact.R
import com.example.contact.adapter.add_plan.add_friend_to_plan.ChoiceFriendListAdapter
import com.example.contact.adapter.add_plan.add_friend_to_plan.FriendListToPlanAdapter
import com.example.contact.databinding.ActivityAddFriendToPlanBinding
import com.example.contact.util.firebase.FirebaseRepository
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddFriendToPlan : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendToPlanBinding
    private val viewModel: AddFriendToPlanViewModel by viewModels()
    @Inject lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendToPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.addFriendToPlanToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /**
         * 친구 아이디 검색
         */
        binding.friendSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {return false}

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getFriendSearch(query)

                viewModel.searchData.observe(this@AddFriendToPlan){ userInfo ->
                    val snackbar = Snackbar.make(binding.root, "Test", Snackbar.LENGTH_LONG)
                    snackbar.setAction("추가"){
                        viewModel.setOnClick(userInfo.uid!!)
                    }

                    snackbar.show()
                }
                return false
            }

        })

        /**
         * 선택한 친구 리스트
         */
        val choiceFriendListAdapter = ChoiceFriendListAdapter(viewModel, firebaseRepository)
        binding.withFriendRecycler.adapter = choiceFriendListAdapter

        viewModel.checkCurrentFriend.observe(this){
            choiceFriendListAdapter.setFriendList(it)
        }


        /**
         * 친구 목록
         */
        val friendListToPlanAdapter = FriendListToPlanAdapter(viewModel, this, firebaseRepository)
        binding.friendListRecycler.adapter = friendListToPlanAdapter

        viewModel.friendList.observe(this){
            friendListToPlanAdapter.setUidList(it?.friend)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_check ->{
                val list = viewModel.checkCurrentFriend.value

                intent.apply {
                    putExtra("friendList", list)
                    setResult(RESULT_OK, intent)
                }
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_check_menu, menu)
        return true
    }
}