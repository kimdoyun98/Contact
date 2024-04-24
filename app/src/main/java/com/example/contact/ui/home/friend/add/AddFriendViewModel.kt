package com.example.contact.ui.home.friend.add

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.BuildConfig
import com.example.contact.data.fcm.Fcm
import com.example.contact.data.user.Friend
import com.example.contact.data.user.Request
import com.example.contact.data.user.UserInfo
import com.example.contact.di.FirebaseCloudMessage
import com.example.contact.util.firebase.FirebaseRepository
import com.example.contact.util.retrofit.RetrofitUrl
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    @FirebaseCloudMessage private val retrofitUrl: RetrofitUrl
): ViewModel() {
    // 나의 Document
    private val myUid = firebaseRepository.getMyInfo.uid
    private var userUid = ""
    private val myFriendReq = firebaseRepository.getUserFriend(myUid).document("request")

    private val _searchResult = MutableLiveData(false)
    val searchResult: LiveData<Boolean> = _searchResult

    private val _searchData = MutableLiveData<UserInfo>(
        UserInfo(null, null, null, null)
    )
    val searchData: LiveData<UserInfo> = _searchData

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    // 친구 요청 중인 친구
    private val _addFriendStatus = MutableLiveData(false)
    val addFriendStatus: LiveData<Boolean> = _addFriendStatus

    // 이미 친구
    val friendStatus: LiveData<Friend?> = firebaseRepository.getUserFriend(myUid).document("friend")
                    .asFlow<Friend>().asLiveData()

    // 검색한 친구 Document
    private lateinit var friendUser: DocumentReference
    private lateinit var friendRes: DocumentReference

    /**
     * 유저 검색
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFriendSearch(query: String?){
        _loading.value = true
        viewModelScope.launch {
            firebaseRepository.searchUser(query!!).asFlow<UserInfo>().collect{
                val userInfo = it[0]
                _searchResult.value = true
                userUid = userInfo.uid!!

                _searchData.value = UserInfo(
                    userUid, userInfo.uniqueID, userInfo.email, userInfo.displayName
                )


                // 이미 친추 요청한 친구
                myFriendReq.asFlow<Request>().collect{ request ->
                    if(request != null) _addFriendStatus.value = request.request.contains(userUid)
                }
            }
        }
        _loading.value = false
    }

    /**
     * 친구 추가 버튼 (이미 요청해놓은 상태라면 요청취소)
     */
    fun addFriend(context: Context){
        // 친구 Document
        friendUser = firebaseRepository.getUserInfo(userUid)
        friendRes = firebaseRepository.getUserFriend(userUid).document("response")

        if(addFriendStatus.value!!){
            //친구 요청 취소
            AlertDialog.Builder(context)
                .setTitle("친구 요청 취소")
                .setMessage("친구 요청을 취소하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    //DB 삭제
                    requestCancel()
                    _addFriendStatus.value = false
                }
                .setNegativeButton("아니오") { _, _ ->
                    //TODO Negavive Action
                }
                .create()
                .show()
        }
        else{
            viewModelScope.launch(Dispatchers.IO) {
                //친구 요청 송신 Field
                myFriendReq.get().addOnSuccessListener { document ->
                    val reqList = ArrayList<String>()
                    if(document.data != null) {
                        reqList.addAll(document.data!!["request"] as ArrayList<String>)
                    }

                    reqList.add(userUid)
                    myFriendReq.set(hashMapOf("request" to reqList))
                }

                //친구 요청 수신 Field
                friendRes.get().addOnSuccessListener { document ->
                    val resList = ArrayList<String>()
                    if(document.data != null) {
                        resList.addAll(document.data!!["response"] as ArrayList<String>)
                    }

                    resList.add(myUid)
                    friendRes.set(hashMapOf("response" to resList))
                }

                //Firebase Cloud Messaging
                friendUser
                    .collection("CloudMessaging").document("Token")
                    .get().addOnSuccessListener { tokenDocument ->
                        val token = tokenDocument.data?.get("token").toString()

                        viewModelScope.launch {
                            val response = retrofitUrl
                                .setFCM(
                                    Fcm(
                                        token,
                                        Fcm.FcmMessage(
                                            "친구 요청",
                                            "${searchData.value?.displayName}님이 친구 요청을 하였습니다."
                                        )
                                    ),
                                    BuildConfig.FirebaseCloudMessaging
                                )
//                        if(response.isSuccessful){
//                            Toast.makeText(MyApplication.getInstance(), "성공", Toast.LENGTH_SHORT).show()
//                        }
//                        else Toast.makeText(MyApplication.getInstance(), "실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        _addFriendStatus.value = true
    }

    /**
     * 친구 요청 취소
     */
    private fun requestCancel() = viewModelScope.launch(Dispatchers.IO) {
        myFriendReq.get().addOnSuccessListener { document ->
            val list = document.data!!["request"] as ArrayList<String>

            val index = list.indexOf(userUid)
            list.removeAt(index)

            myFriendReq.set(hashMapOf("request" to list))
        }

        friendRes.get().addOnSuccessListener { document ->
            val list = document.data!!["response"] as ArrayList<String>

            val index = list.indexOf(myUid)

            list.removeAt(index)

            friendRes.set(hashMapOf("response" to list))
        }
    }
}