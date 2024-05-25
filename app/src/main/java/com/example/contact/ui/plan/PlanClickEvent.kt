package com.example.contact.ui.plan

import com.google.firebase.firestore.DocumentSnapshot

interface PlanClickEvent {
    fun onPlanClickEvent(doc: DocumentSnapshot)
}