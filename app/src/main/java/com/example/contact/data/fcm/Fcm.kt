package com.example.contact.data.fcm

data class Fcm(
    val to: String,
    val data: FcmMessage
){
    data class FcmMessage(
        val title: String,
        val body: String
    )
}
