package com.example.contact.util.firebase

import com.example.contact.ui.home.add_plan.Plan
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    val fireAuth = FirebaseAuth.getInstance()
    val getMyInfo = fireAuth.currentUser!!

    companion object {
        private var instance: FirebaseRepository? = null

        fun getInstance(): FirebaseRepository =
            instance ?: synchronized(this){
                instance ?: FirebaseRepository().also {
                    instance = it
                }
            }
    }

    /**
     * UserInfo
     */
    fun getUserInfo(uid: String): DocumentReference =
        fireStore.collection("Users").document(uid)


    fun getUserFriend(uid:String): CollectionReference = getUserInfo(uid).collection("Friend")

    fun searchUser(uniqueID: String) =
        fireStore.collection("Users").whereEqualTo("uniqueID", uniqueID)


    /**
     * Plan
     */
    fun setPlan(plan: Plan): Task<DocumentReference> =
        fireStore.collection("Plan").add(plan)

}