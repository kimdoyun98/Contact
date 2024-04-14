package com.example.contact.util

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.example.contact.BuildConfig
import com.example.contact.ui.MainActivity
import com.example.contact.ui.sign.Login
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class MyApplication: Application() {
    companion object{
        private lateinit var myApplication: MyApplication
        fun getInstance():MyApplication = myApplication
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KakaoNativeAppKey)
        myApplication = this
    }
}