package com.example.contact.ui.plan.detail.info.dutchpay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.dutch.DutchData
import com.example.contact.util.firebase.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DutchAddViewModel @Inject constructor(
    private val firebaseRepository: PlanRepository
): ViewModel() {
    private val _checkCurrentFriend = MutableLiveData<ArrayList<String>>(arrayListOf())
    val checkCurrentFriend: LiveData<ArrayList<String>> = _checkCurrentFriend

    private val _autoStatus = MutableLiveData<Boolean>(true)
    val autoStatus: LiveData<Boolean> = _autoStatus

    private lateinit var planId: String
    val map = HashMap<String, Int>()

    private val _saveStatus = MutableLiveData(false)
    val saveStatus: LiveData<Boolean> = _saveStatus

    fun changeAuto(status: Boolean){
        _autoStatus.value = status
    }

    fun setPlanId(planId: String){
        this.planId = planId
    }

    fun setOnClick(uid: String){
        val list = _checkCurrentFriend.value!!

        if(list.contains(uid)) list.remove(uid)
        else list.add(uid)

        _checkCurrentFriend.value = list
    }

    fun save(memo: String, total: Int){
        val member = checkCurrentFriend.value!!
        if(autoStatus.value!!){
            for(i in member){
                map[i] = total/member.size
            }
        }

        val data = DutchData(
            memo,
            map
        )

        val date = Date(System.currentTimeMillis())
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val time = sdf.format(date)
        viewModelScope.launch{
            firebaseRepository.setDutchPay(planId, time, data)
                .addOnSuccessListener {
                    _saveStatus.value = true
                }
                .addOnFailureListener {
                    _saveStatus.value = false
                }
        }
    }
}