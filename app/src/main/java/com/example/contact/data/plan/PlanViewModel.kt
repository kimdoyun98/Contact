package com.example.contact.data.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.snapshotAsFlow
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val myUid = firebaseRepository.getMyInfo.uid
    val myPlanList = firebaseRepository.getMyPlan(myUid).snapshotAsFlow().asLiveData()


}