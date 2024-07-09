package com.example.contact.ui.chat.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.contact.R
import com.example.contact.adapter.chat.ChatAdapter
import com.example.contact.adapter.chat.search.RecentSearchAdapter
import com.example.contact.adapter.chat.search.SearchFriendListAdapter
import com.example.contact.databinding.ActivitySearchChatBinding
import com.example.contact.ui.chat.chatting.Chatting
import com.example.contact.ui.plan.PlanClickEvent
import com.example.contact.util.MyApplication
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.JsonArray
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONArray

@AndroidEntryPoint
class SearchChat : AppCompatActivity() {
    private lateinit var binding: ActivitySearchChatBinding
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var sv: SearchView
    private lateinit var recentAdapter: RecentSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /**
         * 최근 검색어
         */
        recentAdapter = RecentSearchAdapter(viewModel)
        binding.recentRecycler.adapter = recentAdapter

        recentAdapter.setClickEvent(object : RecentButtonClick{
            override fun onClickRecentSearch(query: String) {
                sv.setQuery(query, true)
                viewModel.setSearch(query)
            }
        })


        /**
         * 채팅 목록
         */
        val chatListAdapter = ChatAdapter()
        binding.roomRecycler.adapter = chatListAdapter

        chatListAdapter.setClickEvent(object : PlanClickEvent {
            override fun onPlanClickEvent(doc: DocumentSnapshot) {
                val chatIntent = Intent(MyApplication.getInstance(), Chatting::class.java)
                chatIntent.putExtra("docId", doc.id)
                startActivity(chatIntent)
            }
        })

        /**
         * 친구 리스트
         */
        val friendAdapter = SearchFriendListAdapter()
        binding.friendRecycler.adapter = friendAdapter

        lifecycleScope.launch {
            launch {
                viewModel.userInfo.collect{
                    friendAdapter.setData(it)
                }
            }
            launch {
                viewModel.searchChat.collect{
                    chatListAdapter.setList(it.toMutableList())
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val mSearch = menu!!.findItem(R.id.toolbar_search)
        sv = mSearch.actionView as SearchView

        sv.setIconifiedByDefault(false)
        sv.queryHint = "검색"
        sv.maxWidth = Int.MAX_VALUE
        sv.isFocusable = true

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearch(query!!)
                recentAdapter.addRecentSearch(query)
                sv.clearFocus() //포커스 제거 >> 키보드 내려감
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        // 키보드 내리기
        sv.setOnQueryTextFocusChangeListener{ _, hasFocus ->
            if(hasFocus){
                val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}