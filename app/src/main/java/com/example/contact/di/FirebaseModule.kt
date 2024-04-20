package com.example.contact.di

import com.example.contact.util.RetrofitManager.getFirebaseToken
import com.example.contact.util.RetrofitUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    fun firebaseTokenManage(): RetrofitUrl = getFirebaseToken().create(RetrofitUrl::class.java)

    @Singleton
    @Provides
    fun firebaseFireStoreInstance(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}