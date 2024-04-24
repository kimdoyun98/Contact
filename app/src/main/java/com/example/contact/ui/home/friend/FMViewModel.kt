package com.example.contact.ui.home.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.Friend
import com.example.contact.data.user.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FMViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore
): ViewModel() {
    private val _reqFriend = MutableLiveData<MutableList<String>>()
    val reqFriend: LiveData<MutableList<String>> = _reqFriend

    private val _reqFriendStatus = MutableLiveData(false)
    val reqFriendStatus: LiveData<Boolean> = _reqFriendStatus

    private val myFriend = firebaseStore.collection("Users").document(firebaseAuth.currentUser!!.uid)
        .collection("Friend")
    val friendList: LiveData<Friend?> = myFriend.document("friend").asFlow<Friend>().asLiveData()

    private var resDocument: DocumentReference

    init {
        val uid = firebaseAuth.currentUser!!.uid
        resDocument = firebaseStore.collection("Users").document(uid)
            .collection("Friend").document("response")

        viewModelScope.launch {
            resDocument.asFlow<Response>().collect {
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
        val myFriend = firebaseStore.collection("Users").document(firebaseAuth.currentUser!!.uid)
            .collection("Friend")
        val userFriend = firebaseStore.collection("Users").document(uid)
            .collection("Friend")

        myFriend.document("response").get().addOnSuccessListener {
            val list = it.data!!["response"] as ArrayList<String>

            val index = list.indexOf(uid)
            list.removeAt(index)
            myFriend.document("response").set(hashMapOf("response" to list))
        }

        userFriend.document("request").get().addOnSuccessListener {
            val list = it.data!!["request"] as ArrayList<String>

            val index = list.indexOf(firebaseAuth.currentUser!!.uid)
            list.removeAt(index)
            userFriend.document("request").set(hashMapOf("request" to list))
        }

        if(boolean){
            //firend Document에 친구 삽입
            myFriend.document("friend").get()
                .addOnSuccessListener { document ->
                    val list = mutableSetOf<String>()
                    if(document.data != null){
                        list.addAll(document.data!!["friend"] as ArrayList<String>)
                    }

                    list.add(uid)
                    myFriend.document("friend").set(hashMapOf("friend" to list.toList()))
                }

            userFriend.document("friend").get()
                .addOnSuccessListener { document ->
                    val list = mutableSetOf<String>()
                    if(document.data != null){
                        list.addAll(document.data!!["friend"] as ArrayList<String>)
                    }

                    list.add(firebaseAuth.currentUser!!.uid)
                    userFriend.document("friend").set(hashMapOf("friend" to list.toList()))
                }
        }
    }
}