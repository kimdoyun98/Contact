package com.example.contact.ui.plan.detail.info

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.DetailPlan
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PlanDetailInfoViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private lateinit var docInfo: DocInfo

    private val _currentPhoto = MutableLiveData<Uri>()
    var currentPhoto: LiveData<Uri> = _currentPhoto

    lateinit var imgList: List<Uri>

    fun getDetailPlan(planId: String, date: String, dplanId: String): LiveData<DetailPlan?>
        = firebaseRepository.getDetailPlan(planId, date, dplanId)

    fun setDocInfo(doc:DocInfo){
        docInfo = doc
    }

    fun setCurrentPhoto(uri: Uri){
        _currentPhoto.value = uri
    }

    fun imageSave(){
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val fileName = "IMG_" + sdf.format(Date()) + ".png"
        viewModelScope.launch {
            firebaseRepository.setDetailPlanImage(docInfo.planId, docInfo.date, docInfo.dplanId, fileName, currentPhoto.value!!)
                .addOnSuccessListener { imageTask ->
                    imageTask.storage.downloadUrl
                        .addOnSuccessListener { test ->
                            Log.e("ImgUri", test.toString())
                            firebaseRepository.getPlan(docInfo.planId)
                                .collection(docInfo.date).document(docInfo.dplanId)
                                .update("images", FieldValue.arrayUnion(test))
                        }
                }
                .addOnFailureListener{
                    Log.e("Image Add", "Fail")
                }
        }
    }
}

data class DocInfo(
    val planId: String,
    val date: String,
    val dplanId:String
)