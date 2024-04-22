package com.example.contact.util.retrofit

import com.example.contact.data.fcm.FCMResponse
import com.example.contact.data.FirebaseToken
import com.example.contact.data.fcm.Fcm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitUrl {
    @FormUrlEncoded
    @POST("/verifyToken")
    suspend fun getCustomToken(@Field("token") kakaoToken: String): Response<FirebaseToken>

    @POST("/fcm/send")
    suspend fun setFCM(@Body fcm: Fcm,
                       @Header("Authorization") key: String): Response<FCMResponse>
}