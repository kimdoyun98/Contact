package com.example.contact.ui.chat.chatting

import androidx.lifecycle.ViewModel
import com.example.contact.util.firebase.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {
    val getMyChat = chatRepository.getChatList(chatRepository.fireAuth.currentUser!!.uid)


}