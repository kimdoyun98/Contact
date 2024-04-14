package com.example.contact.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.ui.sign.Login
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkToken()
    }

    private fun checkToken(){
        val loginIntent = Intent(this, Login::class.java)
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) startActivity(loginIntent)
                    else {
                        //기타 에러
                        startActivity(loginIntent)
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    startActivity(mainIntent)
                }
            }
        }
        else startActivity(loginIntent)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}