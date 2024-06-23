package com.example.contact.ui.plan.detail.info.dutchpay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.data.plan.dutch.ReceiptData
import com.example.contact.util.firebase.PlanRepository
import com.google.firebase.firestore.QueryDocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DutchPayViewModel @Inject constructor(
    private val firebaseRepository: PlanRepository
): ViewModel() {
    private val _displayName = MutableLiveData<ArrayList<String>>(arrayListOf())
    val displayName: LiveData<ArrayList<String>> = _displayName

    private val _memberPay = MutableLiveData<HashMap<String, Int>>()
    val memberPay: LiveData<HashMap<String, Int>> = _memberPay

    val receipt = ArrayList<ReceiptData>()

    fun setDisplayName(member: ArrayList<String>){
        _displayName.value = member

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