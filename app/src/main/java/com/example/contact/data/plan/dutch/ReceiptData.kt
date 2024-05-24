package com.example.contact.data.plan.dutch

import java.io.Serializable

data class ReceiptData(
    val date: String,
    val memo: String,
    val member: String
    ): Serializable
