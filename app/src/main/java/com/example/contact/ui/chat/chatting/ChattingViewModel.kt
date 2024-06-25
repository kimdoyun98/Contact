package com.example.contact.ui.chat.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.contact.data.chat.ChatData
import com.example.contact.util.firebase.ChatRepository
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {
    private var docId: String? = null
    private lateinit var chatData: ChatData

    fun setDocID(id: String){
        docId = id
        chatData = chatRepository.getChatInfo(id).value!!
    }

    fun getChatInfo() = chatData

    fun getChatMessage(): LiveData<QuerySnapshot?>? = docId?.let { chatRepository.getChatMessage(docId!!) }

}