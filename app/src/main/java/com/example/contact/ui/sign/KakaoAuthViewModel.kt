package com.example.contact.ui.sign

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.util.MyApplication
import com.example.contact.util.RetrofitUrl
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
class KakaoAuthViewModel @Inject constructor(private val retrofit: RetrofitUrl): ViewModel() {
    private val _logInStatus = MutableStateFlow(false)
    val logInStatus: StateFlow<Boolean> = _logInStatus

    fun kakaoLogin(context: Context){
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
                                    Log.i("Firebase token", firebaseToken.toString())

                                    MyApplication.prefs.setString("fb_token", firebaseToken)
                                    continuation.resume(true)
                                }
                                else{
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

    private fun onError(message: String) {
        //countryLoadError.value = message
        //loading.value = false
    }
}