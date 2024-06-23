package com.example.contact.ui.home.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.Friend
import com.example.contact.data.user.Response
import com.example.contact.util.firebase.UserInfoRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FMViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
): ViewModel() {
    private val myUid = userInfoRepository.fireAuth.currentUser?.uid!!
    private val myFriend = userInfoRepository.getUserFriend(myUid)

    private val _reqFriend = MutableLiveData<MutableList<String>>()
    val reqFriend: LiveData<MutableList<String>> = _reqFriend

    private val _reqFriendStatus = MutableLiveData(false)
    val reqFriendStatus: LiveData<Boolean> = _reqFriendStatus

    val friendList: LiveData<Friend?> = myFriend.document("friend").asFlow<Friend>().asLiveData()


    init {
        viewModelScope.launch {
            myFriend.document("response")
                .asFlow<Response>().collect {
                    if(it != null) {
                        if(it.response.size != 0){
                            _reqFriend.value = it.response
                            _reqFriendStatus.value = true
                        }
                    }
                }
        }
    }

    /**
     * 친구 요청 수락&거절 버튼 클릭 작업
     */
    fun requestButton(uid: String, boolean: Boolean){
        val userFriend = userInfoRepository.getUserFriend(uid)
        Log.e("MyUid", myUid)
        myFriend.document("response").update("response", FieldValue.arrayRemove(uid))
        userFriend.document("request").update("request", FieldValue.arrayRemove(myUid))

        if(boolean){
            //firend Document에 친구 삽입
            userInfoRepository.getUserInfo(uid).get().addOnSuccessListener {
                val userDisplayName = it.data!!["displayName"] as String
                myFriend.document("friend").get().addOnSuccessListener { document ->
                    var map = HashMap<String, String>()
                    if(!document.data.isNullOrEmpty()){
                        Log.e("document.data", document.data.toString())
                        map = document.data!!["friend"] as HashMap<String, String>
                    }

                    map[uid] = userDisplayName
                    myFriend.document("friend").set(hashMapOf("friend" to map))
                }
            }

            userFriend.document("friend").get()
                .addOnSuccessListener { document ->
                    var map = HashMap<String, String>()
                    if(!document.data.isNullOrEmpty()){
                        map = document.data!!["friend"] as HashMap<String, String>
                    }

                    map[myUid] = userInfoRepository.fireAuth.currentUser?.displayName!!
                    userFriend.document("friend").set(hashMapOf("friend" to map))
                }
        }
    }
}