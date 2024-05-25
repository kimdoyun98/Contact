package com.example.contact.ui.home.add_plan

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.BuildConfig
import com.example.contact.data.fcm.Fcm
import com.example.contact.data.plan.PlanData
import com.example.contact.di.FirebaseCloudMessage
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import com.example.contact.util.retrofit.RetrofitUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AddPlanViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    @FirebaseCloudMessage private val retrofitUrl: RetrofitUrl
): ViewModel() {
    private val _friendList = MutableLiveData<ArrayList<String>>(arrayListOf())
    val friendList: LiveData<ArrayList<String>> = _friendList

    private val _dateCheckStatus = MutableLiveData<Boolean>(false)
    val dataCheckStatus: LiveData<Boolean> = _dateCheckStatus

    private val _timeCheckStatus = MutableLiveData<Boolean>(false)
    val timeCheckStatus: LiveData<Boolean> = _timeCheckStatus

    private val _registerStatus = MutableStateFlow<Boolean>(false)
    val registerStatus: StateFlow<Boolean> = _registerStatus

    /**
     * 친구
     */
    fun setFriendList(list: ArrayList<String>){
        val newList = ArrayList<String>()
        newList.apply {
            addAll(friendList.value!!)
            newList.addAll(list)
        }

        _friendList.value = newList.toSet().toCollection(ArrayList())
    }

    fun cancel(uid: String){
        val list = friendList.value!!
        list.remove(uid)

        _friendList.value = list
    }

    /**
     * Check Box
     */
    fun setDateCheckBox(boolean: Boolean){
        _dateCheckStatus.value = boolean
    }

    fun setTimeCheckBox(boolean: Boolean){
        _timeCheckStatus.value = boolean
    }

    /**
     * 등록 버튼
     */
    fun registerButton(title: String, start: String?, end: String?, time: List<String>?){
        viewModelScope.launch{
            _registerStatus.emit(registerFirebaseDB(title, start, end, time))
        }
    }
    private suspend fun registerFirebaseDB(title: String, start: String?, end: String?, time: List<String>?): Boolean =
        suspendCoroutine { continuation ->
            val member = arrayListOf(firebaseRepository.getMyInfo.uid)

            val t = if(time.isNullOrEmpty()) null else "${time[0]}:${time[1]}"
            val date = if(start == null) arrayListOf<String>() else arrayListOf(start, end)

            val planData = PlanData(
                title,
                member,
                friendList.value!!,
                date,
                t
            )

            viewModelScope.launch {
                firebaseRepository.setPlan(planData)
                    .addOnSuccessListener {
                        friendList.value!!.forEach {
                            sendInviteMessage(it)
                        }
                        continuation.resume(true)
                        Toast.makeText(MyApplication.getInstance(), "성공!", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                        Toast.makeText(MyApplication.getInstance(), "실패!", Toast.LENGTH_SHORT).show()
                    }
            }
        }


    private fun sendInviteMessage(uid: String){
        val friendInfo = firebaseRepository.getUserInfo(uid)

        friendInfo
            .collection("CloudMessaging").document("Token")
            .get().addOnSuccessListener { tokenDocument ->
                val token = tokenDocument.data?.get("token").toString()

                viewModelScope.launch {
                    retrofitUrl
                        .setFCM(
                            Fcm(
                                token,
                                Fcm.FcmMessage(
                                    "초대장",
                                    "${firebaseRepository.getMyInfo.displayName}님이 모임에 초대하였습니다."
                                )
                            ),
                            BuildConfig.FirebaseCloudMessaging
                        )
                }
            }

    }

}