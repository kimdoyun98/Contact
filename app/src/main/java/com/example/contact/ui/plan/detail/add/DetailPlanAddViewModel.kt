package com.example.contact.ui.plan.detail.add

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.DetailPlan
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class DetailPlanAddViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private var date = ""
    private var planId = ""
    private var _imgUri = MutableLiveData<MutableList<Uri>>(mutableListOf())
    val imgUri: LiveData<MutableList<Uri>> = _imgUri

    private val _registerStatus = MutableStateFlow<Boolean>(false)
    val registerStatus: StateFlow<Boolean> = _registerStatus

    fun setDate(date: String, planId: String){
        this.date = date
        this.planId = planId
    }

    fun addImgUri(uri: Uri){
        val list = imgUri.value
        list?.add(uri)
        _imgUri.value = list!!
    }
    fun register(time: List<String>, location: String, address: String?){
        viewModelScope.launch{
            _registerStatus.emit(registerFB(time, location, address))
        }
    }

    private suspend fun registerFB(time: List<String>, location: String, address: String?): Boolean =
        suspendCoroutine { continuation ->
            viewModelScope.launch {
                firebaseRepository.getPlan(planId)
                    .collection(date).add(
                        DetailPlan(
                            "${time[0]}:${time[1]}",
                            location,
                            address
                        )
                    )
                    .addOnSuccessListener {
                        continuation.resume(true)
                        Toast.makeText(MyApplication.getInstance(), "성공!", Toast.LENGTH_SHORT)
                            .show()
                        /**
                         * 이미지 저장
                         */
                        val dplanId = it.id
                        imgUri.value?.forEach { uri ->
                            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                            val fileName = "IMG_" + sdf.format(Date()) + ".png"
                            firebaseRepository.setDetailPlanImage(planId, date, dplanId, fileName, uri)
                                .addOnSuccessListener { imageTask ->
                                    imageTask.storage.downloadUrl
                                        .addOnSuccessListener { test ->
                                            Log.e("ImgUri", test.toString())
                                            firebaseRepository.getPlan(planId)
                                                .collection(date).document(dplanId)
                                                .update("images", FieldValue.arrayUnion(test))
                                        }
                                }
                                .addOnFailureListener{
                                    Log.e("Image Add", "Fail")
                                }
                        }

                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                        Toast.makeText(MyApplication.getInstance(), "실패!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
}