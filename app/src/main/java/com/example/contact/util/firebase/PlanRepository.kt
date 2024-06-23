package com.example.contact.util.firebase

import android.net.Uri
import androidx.lifecycle.asLiveData
import com.example.contact.data.plan.DetailPlan
import com.example.contact.data.plan.PlanData
import com.example.contact.data.plan.dutch.DutchData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.UploadTask
import io.github.horaciocome1.fireflow.asFlow
import io.github.horaciocome1.fireflow.snapshotAsFlow

class PlanRepository: FirebaseRepository() {

    companion object {
        private var instance: PlanRepository? = null

        fun getInstance(): PlanRepository =
            instance ?: synchronized(this){
                instance ?: PlanRepository().also {
                    instance = it
                }
            }
    }

    fun setPlan(planData: PlanData): Task<DocumentReference> =
        fireStore.collection("Plan").add(planData)

    fun getPlan(planId: String) =
        fireStore.collection("Plan").document(planId)

    fun getInvitePlan(uid: String) =
        fireStore.collection("Plan").whereArrayContains("invite", uid)

    fun checkOutPlan(planId: String, uid: String) =
        fireStore.collection("Plan").document(planId)
            .update("member", FieldValue.arrayRemove(uid))

    fun updateTitle(planId: String, title: String) =
        fireStore.collection("Plan").document(planId)
            .update("title", title)

    fun getMyPlan(uid: String) =
        fireStore.collection("Plan").whereArrayContains("member", uid)

    /**
     * DetailPlan
     */
    fun detailPlanList(planId: String, date: String) = getPlan(planId)
        .collection(date).snapshotAsFlow().asLiveData()

    fun getDetailPlan(planId: String, date: String, dplanId: String) =
        fireStore.collection("Plan").document(planId).collection(date).document(dplanId).asFlow<DetailPlan>().asLiveData()

    fun updateDetailInfo(planId: String, date: String, dplanId: String, data: Map<String, String>) =
        fireStore.collection("Plan").document(planId).collection(date).document(dplanId)
            .update(data)

    fun deleteDetailPlan(planId:String, date: String, docId:String) =
        fireStore.collection("Plan").document(planId)
            .collection(date).document(docId).delete()

    /**
     * Plan Dutch Pay
     */
    fun setDutchPay(planId: String, time: String, data: DutchData) =
        fireStore.collection("Plan").document(planId).collection("DotchPay").document(time).set(data)

    fun getDutchPay(planId: String) =
        fireStore.collection("Plan").document(planId)
            .collection("DotchPay").snapshotAsFlow().asLiveData()


    /**
     * DetailPlan Image
     */
    fun setDetailPlanImage(planId: String, date: String, dplanId: String, fileName: String, uri: Uri): UploadTask =
        fireStorage.getReference("$planId/$date/$dplanId/$fileName").putFile(uri)
}