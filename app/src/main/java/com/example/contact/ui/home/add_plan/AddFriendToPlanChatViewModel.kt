package com.example.contact.ui.home.add_plan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.Friend
import com.example.contact.data.user.UserInfo
import com.example.contact.util.firebase.ChatRepository
import com.example.contact.util.firebase.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AddFriendToPlanChatViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val chatRepository: ChatRepository
): ViewModel() {
    private val myUid = userInfoRepository.fireAuth.currentUser?.uid!!
    val friendList = userInfoRepository.getUserFriend(myUid).document("friend")
        .asFlow<Friend>().asLiveData()

    private val _checkCurrentFriend = MutableLiveData<ArrayList<String>>(arrayListOf())
    val checkCurrentFriend: LiveData<ArrayList<String>> = _checkCurrentFriend

    private val _searchResult = MutableLiveData(false)
    val searchResult: LiveData<Boolean> = _searchResult

    private val _searchData = MutableLiveData<UserInfo>(
        UserInfo(null, null, null, null)
    )
    val searchData: LiveData<UserInfo> = _searchData

    fun getFriendSearch(query: String?){
        viewModelScope.launch {
            userInfoRepository.searchUser(query!!).asFlow<UserInfo>().collect{
                val userInfo = it[0]
                _searchResult.value = true
                val userUid = userInfo.uid!!

                _searchData.value = UserInfo(
                    userUid, userInfo.uniqueID, userInfo.email, userInfo.displayName
                )
            }
        }
    }


    fun setOnClick(uid: String){
        //TODO 체크 유무 확인
        val list = _checkCurrentFriend.value!!

        if(list.contains(uid)) list.remove(uid)
        else list.add(uid)

        _checkCurrentFriend.value = list
    }

    fun cancel(uid: String){
        val list = _checkCurrentFriend.value!!
        list.remove(uid)

        _checkCurrentFriend.value = list
    }

    /**
     * Chat
     */
    private val _chatStatus = MutableLiveData(false)
    val chatStatus: LiveData<Boolean> = _chatStatus

    var docId = ""
    fun registerChat(){
        viewModelScope.launch {
            _chatStatus.value = handleRegisterChat()
        }
    }

    private suspend fun handleRegisterChat(): Boolean =
        suspendCoroutine { continuation ->
            val member = HashMap<String, String>()
            val member2 = arrayListOf(myUid)
            val map = friendList.value?.friend
            val memberDisplayName = arrayListOf(userInfoRepository.fireAuth.currentUser?.displayName!!)

            member[myUid] = userInfoRepository.fireAuth.currentUser?.displayName!!

            checkCurrentFriend.value?.forEach {
                if(map!![it] != null) {
                    memberDisplayName.add(map[it]!!)
                    member[it] = map[it]!!
                    member2.add(it)
                }
            }
            val title = memberDisplayName.toString().apply {
                substring(1, this.length-1)
            }

            chatRepository.registerChat(
                title = title,
                member = member,
                member2 = member2
            ).addOnSuccessListener {
                docId = it.id
                continuation.resume(true)
            }
    }
}
