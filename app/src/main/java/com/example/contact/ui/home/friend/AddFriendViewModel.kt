package com.example.contact.ui.home.friend

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.BuildConfig
import com.example.contact.data.fcm.Fcm
import com.example.contact.data.user.UserInfo
import com.example.contact.di.FirebaseCloudMessage
import com.example.contact.util.retrofit.RetrofitUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val application: Application,
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth,
    @FirebaseCloudMessage private val retrofitUrl: RetrofitUrl
): AndroidViewModel(application) {
    private val _searchResult = MutableLiveData(false)
    val searchResult: LiveData<Boolean> = _searchResult

    private val _searchData = MutableLiveData<UserInfo>(
        UserInfo(null, null, null, null)
    )
    val searchData: LiveData<UserInfo> = _searchData

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _addFriendStatus = MutableLiveData(false)
    val addFriendStatus: LiveData<Boolean> = _addFriendStatus

    // 나의 Document
    private val myUser = fireStore.collection("Users").document(fireAuth.currentUser!!.uid)
    private val myFriendReq = myUser.collection("Friend").document("request")

    // 검색한 친구 Document
    private lateinit var friendUser: DocumentReference
    private lateinit var friendRes: DocumentReference


    private fun searchUsers(id: String) = fireStore.collection("Users").whereEqualTo("uniqueID", id).get()
    fun getFriendSearch(query: String?){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            searchUsers(query!!).addOnSuccessListener { documents ->
                    _searchResult.value = true
                    documents.forEach{
                        val data = it.data
                        _searchData.value = UserInfo(
                            it.id, query, "${data["email"]}", "${data["displayName"]}"
                        )
                        //TODO 추가 눌러 놓은 친구라면
                        myFriendReq.get().addOnSuccessListener { document ->
                            if(document.data != null){
                                val request = document.data!!["request"] as ArrayList<String>
                                _addFriendStatus.value = request.contains(it.id)
                            }
                        }
                    }
                }
        }
        _loading.value = false
    }

    fun addFriend(context: Context){
        // 친구 Document
        friendUser = fireStore.collection("Users").document(searchData.value?.uid!!)
        friendRes = friendUser.collection("Friend").document("response")

        if(addFriendStatus.value!!){
            //TODO 친구 요청 취소
            AlertDialog.Builder(context)
                .setTitle("친구 요청 취소")
                .setMessage("친구 요청을 취소하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    //TODO DB 삭제
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
                //TODO 친구 요청 송신 Field
                myFriendReq.get().addOnSuccessListener { document ->
                    val reqList = ArrayList<String>()
                    if(document.data != null) {
                        reqList.addAll(document.data!!["request"] as ArrayList<String>)
                    }

                    reqList.add(searchData.value!!.uid!!)
                    myFriendReq.set(hashMapOf("request" to reqList))
                }

                //TODO 친구 요청 수신 Field
                friendRes.get().addOnSuccessListener { document ->
                    val resList = ArrayList<String>()
                    if(document.data != null) {
                        resList.addAll(document.data!!["response"] as ArrayList<String>)
                    }

                    resList.add(fireAuth.currentUser!!.uid)
                    friendRes.set(hashMapOf("response" to resList))
                }

                //TODO Firebase Cloud Messaging
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

    private fun requestCancel() = viewModelScope.launch(Dispatchers.IO) {
        myFriendReq.get().addOnSuccessListener { document ->
            val list = document.data!!["request"] as ArrayList<String>
            val friendUid = searchData.value!!.uid

            val index = list.indexOf(friendUid)
            list.removeAt(index)

            myFriendReq.set(hashMapOf("request" to list))
        }

        friendRes.get().addOnSuccessListener { document ->
            val list = document.data!!["response"] as ArrayList<String>

            val myUid = fireAuth.currentUser!!.uid

            val index = list.indexOf(myUid)

            list.removeAt(index)

            friendRes.set(hashMapOf("response" to list))
        }
    }
}