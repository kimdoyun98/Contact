package com.example.contact.ui.plan.detail.info.dutchpay

import androidx.lifecycle.ViewModel
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DutchPayViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

}