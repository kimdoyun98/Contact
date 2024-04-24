package com.example.contact.ui.sign

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.data.user.UserInfo
import com.example.contact.di.FirebaseToken
import com.example.contact.util.MyApplication
import com.example.contact.util.retrofit.RetrofitUrl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class KakaoAuthViewModel @Inject constructor(
    @FirebaseToken private val retrofit: RetrofitUrl,
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth
): ViewModel() {
    private val _logInStatus = MutableStateFlow(false)
    val logInStatus: StateFlow<Boolean> = _logInStatus

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun kakaoLogin(context: Context){
        startLoading()
        viewModelScope.launch {
            _logInStatus.emit(handleKakaoLogin(context))
        }
    }

    private suspend fun handleKakaoLogin(context: Context): Boolean =
        suspendCoroutine { continuation ->
            val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if(error != null) {
                    Log.e("Login", "로그인 실패", error)
                    continuation.resume(false)
                }
                else if(token != null) {
                    Log.i("Login", "로그인 성공 ${token.accessToken}")
                    continuation.resume(true)
                }
            }

            if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)){
                UserApiClient.instance.loginWithKakaoTalk(context){ token, error ->
                    if (error != null) {
                        Log.e("Login", "로그인 실패", error)

                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Log.e("viewmodel", "first Error")
                            return@loginWithKakaoTalk
                        }
                        else {
                            Log.e("viewmodel", "second Error")
                            UserApiClient.instance.loginWithKakaoAccount(
                                context,
                                callback = mCallback
                            )
                        }
                    }
                    else if (token != null) {
                        Log.i("Login", "로그인 성공 ${token.accessToken}")
                        viewModelScope.launch {
                            val response = retrofit.getCustomToken(token.accessToken)
                            withContext(Dispatchers.Main){
                                if(response.isSuccessful){
                                    val firebaseToken = response.body()?.token

                                    MyApplication.prefs.setString("kakao_token", token.accessToken)
                                    MyApplication.prefs.setString("fb_token", firebaseToken)

                                    firebaseLogin(firebaseToken)

                                    continuation.resume(true)
                                }
                                else{
                                    onError()
                                    continuation.resume(false)
                                }
                            }
                        }

                    }
                }
            }
            else{
                UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
            }
        }

    fun startLoading(){
        _loading.value = true
    }
    fun onError() {
        _loading.value = false
    }

    private fun firebaseLogin(token: String?){
        /**
         * Token으로 로그인 처리
         */
        fireAuth.signInWithCustomToken(token!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /**
                     * DB에 유저 등록이 안되어 있으면 등록
                     */
                    addFirebaseDB()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Login", "signInWithCustomToken:failure", task.exception)
                }
            }
    }

    fun addFirebaseDB(){
        val currentUser = fireAuth.currentUser

        val user = UserInfo(
            currentUser?.uid,
            currentUser?.uid,
            currentUser?.email,
            currentUser?.displayName
        )

        val userInfo = fireStore.collection("Users").document(currentUser!!.uid)
        userInfo.get()
            .addOnSuccessListener { document ->
                if(document.data == null) {
                    userInfo.set(user)
                }
            }

        /**
         * FCM token 저장
         */
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            fireStore.collection("Users").document(fireAuth.currentUser!!.uid)
                .collection("CloudMessaging").document("Token")
                .set("token" to token)
        })
    }
}