package com.example.contact.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.FieldValue
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
                    firebaseRepository.getPlan(planId).update(
                        mapOf(
                            "member" to FieldValue.arrayUnion(myUid),
                            "displayNames" to FieldValue.arrayUnion(firebaseRepository.fireAuth.currentUser?.displayName!!)
                        )
                    )
                }
            }
    }
}