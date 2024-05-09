package com.example.contact.ui.plan.detail.add

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.plan.DetailPlan
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class DetailPlanAddViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {
    private var date = ""
    private var planId = ""
    private var _imgUri = MutableLiveData<MutableList<String>>(mutableListOf())
    val imgUri: LiveData<MutableList<String>> = _imgUri

    private val _registerStatus = MutableStateFlow<Boolean>(false)
    val registerStatus: StateFlow<Boolean> = _registerStatus

    fun setDate(date: String, planId: String){
        this.date = date
        this.planId = planId
    }

    fun addImgUri(uri: String){
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
                            address,
                            imgUri.value
                        )
                    )
                    .addOnSuccessListener {
                        continuation.resume(true)
                        Toast.makeText(MyApplication.getInstance(), "성공!", Toast.LENGTH_SHORT)
                            .show()

                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                        Toast.makeText(MyApplication.getInstance(), "실패!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
}