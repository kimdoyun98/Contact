package com.example.contact.util

import android.app.Application
import com.example.contact.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    companion object{
        private lateinit var myApplication: MyApplication
        lateinit var prefs: PreferenceUtil
        fun getInstance():MyApplication = myApplication
    }

    override fun onCreate() {
        super.onCreate()

        prefs = PreferenceUtil(this)

        KakaoSdk.init(this, BuildConfig.KakaoNativeAppKey)
        myApplication = this
    }
}