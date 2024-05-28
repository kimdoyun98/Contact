package com.example.contact.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.snapshotAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val myUid = firebaseRepository.fireAuth.currentUser?.uid!!
    val inviteList = firebaseRepository.getInvitePlan(myUid).snapshotAsFlow().asLiveData()


    /**
     * invite plan button
     */
    fun inviteButton(planId: String, accept: Boolean){
        // member에 추가 및 invite uid 삭제
        firebaseRepository.getPlan(planId).get()
            .addOnSuccessListener {
                val list = it.data!!["invite"] as ArrayList<String>
                list.remove(myUid)
                firebaseRepository.getPlan(planId).update("invite", list)

                if(accept){
                    val memberList = it.data!!["member"] as ArrayList<String>
                    memberList.add(myUid)
                    firebaseRepository.getPlan(planId).update("member", memberList)
                }
            }
    }
}