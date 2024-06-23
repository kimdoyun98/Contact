package com.example.contact.ui.plan.detail.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.data.plan.DetailPlan
import com.example.contact.util.firebase.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoUpdateViewModel @Inject constructor(
    private val firebaseRepository: PlanRepository
): ViewModel() {
    private lateinit var docInfo: DocInfo
    private val _status = MutableLiveData(false)
    var status: LiveData<Boolean> = _status

    fun setDocInfo(doc:DocInfo){
        docInfo = doc
    }

    fun getDetailPlan(planId: String, date: String, dplanId: String): LiveData<DetailPlan?>
            = firebaseRepository.getDetailPlan(planId, date, dplanId)

    fun update(time: String, location: String, address: String){
        val data = mapOf(
            "time" to time,
            "location" to location,
            "address" to address
        )
        firebaseRepository.updateDetailInfo(docInfo.planId, docInfo.date, docInfo.dplanId, data)
            .addOnSuccessListener {
                _status.value = true
            }
    }
}