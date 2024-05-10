package com.example.contact.data.plan

import java.io.Serializable

data class DetailPlan(
    val time: String = "",
    val location: String = "",
    val address: String? = "",
    val images: ArrayList<String> = arrayListOf()
): Serializable
