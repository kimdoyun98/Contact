package com.example.contact.ui.chat.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.Friend
import com.example.contact.data.user.UserInfo
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.ChatRepository
import com.example.contact.util.firebase.UserInfoRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import io.github.horaciocome1.fireflow.snapshotAsFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class SearchChatViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val chatRepository: ChatRepository
): ViewModel() {
    private val myUid = chatRepository.fireAuth.currentUser!!.uid

    private var _userInfo = MutableStateFlow<List<Pair<String, String>>>(listOf())
    val userInfo: StateFlow<List<Pair<String, String>>> = _userInfo

    private var _searchChat = MutableStateFlow<List<DocumentSnapshot>>(listOf())
    val searchChat:StateFlow<List<DocumentSnapshot>> = _searchChat


    /**
     * 검색 >> 친구 & 채팅창
     */
    fun setSearch(search: String){
        searchUser(search)
        searchChat(search)
    }

    /**
     * 최근 검색어
     */
    fun saveRecentSearch(query: String){
        val arrList = getSearchData()
        arrList.add(0, query)
        updateRecentSearch(arrList)
    }

    fun deleteRecentSearch(query: String){
        val arrList = getSearchData()
        arrList.remove(query)
        updateRecentSearch(arrList)
    }

    private fun getSearchData(): ArrayList<String>{
        val recent = MyApplication.prefs.getRecentSearch()
        val arrList = ArrayList<String>()

        repeat(recent.length()){
            arrList.add(recent.optString(it))
        }
        return arrList
    }

    private fun updateRecentSearch(arrList: ArrayList<String>){
        val jsonArr = JSONArray()
        arrList.forEach {
            jsonArr.put(it)
        }

        // 저장
        MyApplication.prefs.setRecentSearch(jsonArr.toString())
    }

    /**
     * 친구 검색 내역
     */
    private fun searchUser(search: String){
        viewModelScope.launch {
            userInfoRepository.getUserFriend(myUid).document("friend").asFlow<Friend>()
                .collect{ Friend ->
                    _userInfo.value = Friend?.friend
                        ?.let {  friend ->
                            val friendList = friend.toList()
                            friendList.filter { it.second.contains(search) }
                        } ?: listOf()
                }
        }
    }

    /**
     * 채팅방 검색 내역
     */
    private fun searchChat(search: String){
        viewModelScope.launch {
            chatRepository.getChatList(myUid).collect{ querySnapshot ->
                querySnapshot?.documents
                    ?.let{
                        _searchChat.value =
                            it.filter { doc ->
                                val map = doc.data!!["member"] as HashMap<String, String>
                                val mapList = map.toList().map { i -> i.second.contains(search) }
                                mapList.contains(true)
                            }
                    }
            }
        }
    }

}