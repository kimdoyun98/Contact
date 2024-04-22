package com.example.contact.ui.home.friend

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.BuildConfig
import com.example.contact.data.fcm.Fcm
import com.example.contact.data.user.UserInfo
import com.example.contact.di.FirebaseCloudMessage
import com.example.contact.util.MyApplication
import com.example.contact.util.retrofit.RetrofitUrl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    @FirebaseCloudMessage private val retrofitUrl: RetrofitUrl
): ViewModel() {
    private val _searchResult = MutableLiveData(false)
    val searchResult: LiveData<Boolean> = _searchResult

    private val _searchData = MutableLiveData<UserInfo>(
        UserInfo(null, null, null)
    )
    val searchData: LiveData<UserInfo> = _searchData

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun getFriendSearch(query: String?){
        _loading.value = true
        viewModelScope.launch {
            searchUsers(query!!).addOnSuccessListener { documents ->
                    _searchResult.value = true
                    documents.forEach{
                        val data = it.data
                        _searchData.value = UserInfo(
                            query, "${data["email"]}", "${data["displayName"]}"
                        )
                    }
                }
            _loading.value = false
        }
    }

    fun addFriend(){
        searchUsers(searchData.value?.uniqueID!!).addOnSuccessListener { documents ->
            documents.forEach{
                val document = it.id
                //TODO 친구 요청 송신 Field


                //TODO 친구 요청 수신 Field


                //TODO Firebase Cloud Messaging
                viewModelScope.launch {
                    fireStore.collection("Users").document(document)
                        .collection("CloudMessaging").document("Token")
                        .get().addOnSuccessListener { tokenDocument ->
                            val token = tokenDocument.data?.get("token").toString()

                            viewModelScope.launch {
                                val response = retrofitUrl
                                    .setFCM(
                                        Fcm(
                                            token,
                                            Fcm.FcmMessage(
                                                "title",
                                                "body"
                                            )
                                        ),
                                        BuildConfig.FirebaseCloudMessaging
                                    )
                                if(response.isSuccessful){
                                    Toast.makeText(MyApplication.getInstance(), "성공", Toast.LENGTH_SHORT).show()
                                }
                                else Toast.makeText(MyApplication.getInstance(), "실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun searchUsers(id: String) = fireStore.collection("Users").whereEqualTo("uniqueID", id).get()
}