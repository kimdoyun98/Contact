package com.example.contact.ui.plan.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private var _date = MutableLiveData<String>("")
    val date: LiveData<String> = _date
    var planId = ""

    fun setDate(string: String){
        _date.value = string
    }

    fun getDetailPlan(): LiveData<QuerySnapshot?> = firebaseRepository.detailPlanList(planId, date.value!!)
}