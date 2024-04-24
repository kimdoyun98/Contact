package com.example.contact.di

import com.example.contact.util.firebase.FirebaseRepository
import com.example.contact.util.retrofit.RetrofitManager.firebaseCloudMessaging
import com.example.contact.util.retrofit.RetrofitManager.getFirebaseToken
import com.example.contact.util.retrofit.RetrofitUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    @FirebaseToken
    fun firebaseTokenManage(): RetrofitUrl = getFirebaseToken().create(RetrofitUrl::class.java)

    @Singleton
    @Provides
    @FirebaseCloudMessage
    fun fcm(): RetrofitUrl = firebaseCloudMessaging().create(RetrofitUrl::class.java)

    @Singleton
    @Provides
    fun getFirebaseInstance(): FirebaseRepository = FirebaseRepository.getInstance()
}

@Qualifier
annotation class FirebaseToken

@Qualifier
annotation class FirebaseCloudMessage