package com.example.contact.util.firebase

class ChatRepository: FirebaseRepository() {

    companion object {
        private var instance: ChatRepository? = null

        fun getInstance(): ChatRepository =
            instance ?: synchronized(this){
                instance ?: ChatRepository().also {
                    instance = it
                }
            }
    }
}