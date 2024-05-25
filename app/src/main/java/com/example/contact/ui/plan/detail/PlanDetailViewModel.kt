package com.example.contact.ui.plan.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.PlanData
import com.example.contact.data.user.UserInfo
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private var planId: String? = null
    private val _memberList = MutableLiveData<String>()
    val memberList:LiveData<String> = _memberList

    var planInfo: PlanData? = null

    fun setPlanId(planId: String){
        this.planId = planId
    }

    fun setPlan(planData: PlanData){
        planInfo = planData
    }

    fun getPlanData(): LiveData<PlanData?> = firebaseRepository.getPlan(planId!!).asFlow<PlanData>().asLiveData()

    fun getMemberDisplayName(memberUid: ArrayList<String>){
        val member = StringBuilder()
        memberUid.forEach { uid ->
            viewModelScope.launch {
                firebaseRepository.getUserInfo(uid).asFlow<UserInfo>().collect{
                    val displayName = it!!.displayName.toString()
                    member.append("$displayName ")
                    Log.e("viewModel member1", member.toString())
                    _memberList.value = member.toString()
                }
            }
        }
    }

    fun setDate(start: String, end: String){
        viewModelScope.launch {
            firebaseRepository.getPlan(planId!!).update("date", arrayListOf(start, end))
        }
    }
}