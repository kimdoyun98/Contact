package com.example.contact.ui.plan.detail

import com.example.contact.data.plan.DetailPlan

interface PlanDetailClick {
    fun planDetailClick(id: String, detailPlan: DetailPlan)
    fun planDetailLongClick(docId: String)
}