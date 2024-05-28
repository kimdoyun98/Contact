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
import com.google.firebase.firestore.QuerySnapshot
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

    private val dateMap = HashMap<String, String>()
    private var planInfo: PlanData? = null

    fun setPlanId(planId: String){
        this.planId = planId
    }

    fun getPlanId(): String? = planId

    fun setPlan(planData: PlanData){
        planInfo = planData
    }

    fun getPlan() = planInfo

    fun setDateMap(tabName: String, day: String){
        dateMap.getOrPut(tabName){ day }
    }

    fun getDateMapValue(key: String) = dateMap[key]!!

    fun getPlanData(): LiveData<PlanData?> = firebaseRepository.getPlan(planId!!).asFlow<PlanData>().asLiveData()

    fun getMemberDisplayName(memberUid: ArrayList<String>){
        val member = StringBuilder()
        memberUid.forEach { uid ->
            viewModelScope.launch {
                firebaseRepository.getUserInfo(uid).asFlow<UserInfo>().collect{
                    val displayName = it!!.displayName.toString()
                    member.append("$displayName ")
                    _memberList.value = member.toString()
                }
            }
        }
    }

    /**
     * 일정 없을 때 등록
     */
    fun setDate(start: String, end: String){
        viewModelScope.launch {
            firebaseRepository.getPlan(planId!!).update("date", arrayListOf(start, end))
        }
    }

    /**
     * 퇴장
     */
    fun checkOut() =
        firebaseRepository.checkOutPlan(planId!!, firebaseRepository.fireAuth.currentUser?.uid!!)

    /**
     * Title 변경
     */
    fun updateTitle(title: String) =
        firebaseRepository.updateTitle(planId!!, title)


    /**
     * Fragment Func
     */
    private var _date = MutableLiveData("")
    val date: LiveData<String> = _date

    fun setDate(string: String){
        _date.value = string
    }

    fun getDetailPlan(): LiveData<QuerySnapshot?> = firebaseRepository.detailPlanList(planId!!, date.value!!)

}