package com.example.contact.util

import android.util.Log
import com.kakao.sdk.user.UserApiClient

object UserInfo {
    private var nickname: String? = ""
    private var email: String? = ""

    init {
        UserApiClient.instance.me { user, error ->
            if(error != null){
                Log.e("유저 정보", "실패")
            }
            else if(user != null){
                Log.e("유저 정보", "성공")
                email = user.kakaoAccount?.profile?.nickname
                nickname = user.kakaoAccount?.profile?.nickname
            }
        }
    }

    fun getNickName(): String? = nickname
    fun getEmail(): String? = email
}