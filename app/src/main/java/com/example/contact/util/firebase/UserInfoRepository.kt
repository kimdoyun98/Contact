package com.example.contact.util.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

class UserInfoRepository: FirebaseRepository() {

    companion object {
        private var instance: UserInfoRepository? = null

        fun getInstance(): UserInfoRepository =
            instance ?: synchronized(this){
                instance ?: UserInfoRepository().also {
                    instance = it
                }
            }
    }

    fun getUserInfo(uid: String): DocumentReference =
        fireStore.collection("Users").document(uid)


    fun getUserFriend(uid:String): CollectionReference = getUserInfo(uid).collection("Friend")

    fun searchUser(uniqueID: String) =
        fireStore.collection("Users").whereEqualTo("uniqueID", uniqueID)
}