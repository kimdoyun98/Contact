package com.example.contact.ui.plan.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TabViewModel: ViewModel() {
    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    var planId = ""

    fun setDate(string: String){
        _date.value = string
    }
}