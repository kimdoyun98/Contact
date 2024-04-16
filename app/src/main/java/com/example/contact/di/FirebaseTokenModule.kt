package com.example.contact.di

import com.example.contact.util.RetrofitManager.getFirebaseToken
import com.example.contact.util.RetrofitUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseTokenModule {
    @Singleton
    @Provides
    fun firebaseTokenManage(): RetrofitUrl = getFirebaseToken().create(RetrofitUrl::class.java)

}