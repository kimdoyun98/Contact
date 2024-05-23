package com.example.contact.ui.plan.detail.info.dutchpay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.Plan
import com.example.contact.data.user.UserInfo
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DutchAddViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val _checkCurrentFriend = MutableLiveData<ArrayList<String>>(arrayListOf())
    val checkCurrentFriend: LiveData<ArrayList<String>> = _checkCurrentFriend

    private val _displayName = MutableLiveData<ArrayList<String>>(arrayListOf())
    val displayName: LiveData<ArrayList<String>> = _displayName

    private val _autoStatus = MutableLiveData<Boolean>(true)
    val autoStatus: LiveData<Boolean> = _autoStatus

    fun changeAuto(status: Boolean){
        _autoStatus.value = status
    }

    fun addDisplayName(member: ArrayList<String>){
        viewModelScope.launch {
            member.forEach {
                firebaseRepository.getUserInfo(it).get()
                    .addOnSuccessListener { document ->
                        val list = displayName.value!!
                        list.add(document.data!!["displayName"]!!.toString())
                        _displayName.value = list
                    }
            }
        }
    }

    fun setOnClick(uid: String){
        val list = _checkCurrentFriend.value!!

        if(list.contains(uid)) list.remove(uid)
        else list.add(uid)

        _checkCurrentFriend.value = list
    }

    fun getMemberList(planId: String): Flow<Plan> = firebaseRepository.getPlan(planId).asFlow<Plan>() as Flow<Plan>

    fun save(memo: String, total: Int){
        val map = HashMap<String, Int>()
        val member = displayName.value!!
        for(i in member){
            if(autoStatus.value!!){
                map[i] = total/member.size
            }
            else{
                
            }
        }
    }
}