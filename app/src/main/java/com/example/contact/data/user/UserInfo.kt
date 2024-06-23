package com.example.contact.data.user

data class UserInfo(
    val uid: String? = "",
    val uniqueID: String? = "",
    val email: String? = "",
    val displayName: String? = ""
)

data class Response(
    val response: ArrayList<String> = arrayListOf()
)

data class Request(
    val request: ArrayList<String> = arrayListOf()
)

data class Friend(
    val friend: HashMap<String, String> = hashMapOf()
)