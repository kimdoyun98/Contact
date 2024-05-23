package com.example.contact.ui.plan.detail.info.dutchpay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DutchPayViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val _displayName = MutableLiveData<ArrayList<String>>(arrayListOf())
    val displayName: LiveData<ArrayList<String>> = _displayName

    private val _memberPay = MutableLiveData<HashMap<String, Int>>()
    val memberPay: LiveData<HashMap<String, Int>> = _memberPay

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
    fun initDutch(){
        val map = HashMap<String, Int>()
        displayName.value?.forEach { name ->
            map[name] = 0
        }
        _memberPay.value = map
    }

    fun setMemberPay(list: HashMap<String, Int>){
        val map = memberPay.value!!

        list.toList().forEach{
            map.replace(it.first, map[it.first]!! + it.second)
        }
        _memberPay.value = map
    }

    fun getDutch(planId: String) = firebaseRepository.getDutchPay(planId)

}