package com.example.contact.data

import com.google.gson.annotations.SerializedName

data class FirebaseToken(
    @SerializedName("firebase_token") val token: String
)
