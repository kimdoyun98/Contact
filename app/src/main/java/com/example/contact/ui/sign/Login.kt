package com.example.contact.ui.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.databinding.ActivityLoginBinding
import com.example.contact.ui.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoLogin.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this){ token, error ->
                    if (error != null) {
                        Log.e("Login", "로그인 실패", error)

                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                        else UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                    }
                    else if (token != null) {
                        Log.i("Login", "로그인 성공 ${token.accessToken}")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
            else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
            }
        }
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null) Log.e("Login", "로그인 실패", error)
        else if(token != null) Log.i("Login", "로그인 성공 ${token.accessToken}")
    }
}