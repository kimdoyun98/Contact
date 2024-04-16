package com.example.contact.util

import com.example.contact.data.FirebaseToken
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitUrl {
    @FormUrlEncoded
    @POST("/verifyToken")
    suspend fun getCustomToken(@Field("token") kakaoToken: String): Response<FirebaseToken>
}