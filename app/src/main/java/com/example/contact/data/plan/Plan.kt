package com.example.contact.data.plan

data class Plan(
    val title: String = "",
    val member: ArrayList<String> = arrayListOf(),
    val invite: ArrayList<String> = arrayListOf(),
    val date: java.util.ArrayList<out String?> = arrayListOf(),
    val time: String? = ""
)
