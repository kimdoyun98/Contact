package com.example.contact.util.retrofit

import com.example.contact.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitManager {
    fun getFirebaseToken(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.LocalServerIP)
            .build()

    fun firebaseCloudMessaging(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://fcm.googleapis.com")
            .build()

    private val gson : Gson = GsonBuilder()
        .setLenient()
        .create()
}