package com.example.contact.ui.home.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore
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
            fireStore.collection("Users")
                .whereEqualTo("uniqueID", query!!)
                .get()
                .addOnSuccessListener { documents ->
                    _searchResult.value = true
                    documents.forEach{
                        val data = it.data
                        _searchData.value = UserInfo(
                            null, "${data["email"]}", "${data["displayName"]}"
                        )
                    }
                }
            _loading.value = false
        }
    }
}