package com.example.contact.ui.home.add_plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.Friend
import com.example.contact.data.user.UserInfo
import com.example.contact.util.firebase.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendToPlanViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
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
}
