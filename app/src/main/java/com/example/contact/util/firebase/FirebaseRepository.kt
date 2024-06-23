package com.example.contact.util.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class FirebaseRepository {
    val fireStore = FirebaseFirestore.getInstance()
    val fireStorage = FirebaseStorage.getInstance()
    val fireAuth = FirebaseAuth.getInstance()
}