package com.example.contact.util.firebase

import android.net.Uri
import androidx.lifecycle.asLiveData
import com.example.contact.data.plan.DetailPlan
import com.example.contact.data.plan.Plan
import com.example.contact.data.plan.dutch.DutchData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.horaciocome1.fireflow.asFlow
import io.github.horaciocome1.fireflow.snapshotAsFlow

class FirebaseRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorage = FirebaseStorage.getInstance()

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

    fun getInvitePlan(uid: String) =
        fireStore.collection("Plan").whereArrayContains("invite", uid)

    fun getPlan(planId: String) =
        fireStore.collection("Plan").document(planId)

    fun getMyPlan(uid: String) =
        fireStore.collection("Plan").whereArrayContains("member", uid)

    fun detailPlanList(planId: String, date: String) = getPlan(planId)
        .collection(date).snapshotAsFlow().asLiveData()

    fun getDetailPlan(planId: String, date: String, dplanId: String) =
        fireStore.collection("Plan").document(planId).collection(date).document(dplanId).asFlow<DetailPlan>().asLiveData()

    fun updateDetailInfo(planId: String, date: String, dplanId: String, data: Map<String, String>) =
        fireStore.collection("Plan").document(planId).collection(date).document(dplanId)
            .update(data)

    /**
     * Plan Dutch Pay
     */
    fun setDutchPay(planId: String, time: String, data: DutchData) =
        fireStore.collection("Plan").document(planId).collection("DotchPay").document(time).set(data)


    /**
     * Image
     */
    fun setDetailPlanImage(planId: String, date: String, dplanId: String, fileName: String, uri: Uri): UploadTask =
        fireStorage.getReference("$planId/$date/$dplanId/$fileName").putFile(uri)

}