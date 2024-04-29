package com.example.contact.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private val myUid = firebaseRepository.getMyInfo.uid

    val test = firebaseRepository.getInvitePlan(myUid).asFlow<DocumentSnapshot>().map { it }

    private val _inviteList = MutableLiveData<MutableList<DocumentSnapshot>>(mutableListOf())
    val inviteList: LiveData<MutableList<DocumentSnapshot>> = _inviteList

    fun getMyInviteList(): LiveData<MutableList<DocumentSnapshot>>{
        firebaseRepository.getInvitePlan(myUid).get()
            .addOnSuccessListener {
                it.documents.forEach { documentSnapshot ->
                    val list = inviteList.value!!
                    list.add(documentSnapshot)
                    _inviteList.value = list
                }
            }

        return inviteList
    }

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