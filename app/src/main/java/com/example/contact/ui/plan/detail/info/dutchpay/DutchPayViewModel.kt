package com.example.contact.ui.plan.detail.info.dutchpay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.dutch.ReceiptData
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.QueryDocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class DutchPayViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val _displayName = MutableLiveData<ArrayList<String>>(arrayListOf())
    val displayName: LiveData<ArrayList<String>> = _displayName

    private val _memberPay = MutableLiveData<HashMap<String, Int>>()
    val memberPay: LiveData<HashMap<String, Int>> = _memberPay

    val receipt = ArrayList<ReceiptData>()

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

    fun setMemberPay(list: HashMap<String, Int>, document: QueryDocumentSnapshot){
        val map = memberPay.value!!
        val memberData = StringBuilder()

        list.toList().forEach{
            map.replace(it.first, map[it.first]!! + it.second)
            memberData.append("${it.first} : ${it.second} 원\n")
        }
        _memberPay.value = map

        setReceipt(document, memberData.toString())
    }

    fun getDutch(planId: String) = firebaseRepository.getDutchPay(planId)

    private fun setReceipt(document: QueryDocumentSnapshot, memberData: String){
        val date = document.id
        val data = ReceiptData(
            date = date,
            memo = document.data["memo"]!!.toString(),
            member = memberData
        )
        receipt.add(data)
    }

}