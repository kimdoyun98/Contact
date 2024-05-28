package com.example.contact.data.plan

import java.io.Serializable

data class PlanData(
    val title: String = "",
    val member: ArrayList<String> = arrayListOf(),
    val displayNames: ArrayList<String> = arrayListOf(),
    val invite: ArrayList<String> = arrayListOf(),
    val date: java.util.ArrayList<out String?> = arrayListOf(),
    val time: String? = ""
): Serializable
