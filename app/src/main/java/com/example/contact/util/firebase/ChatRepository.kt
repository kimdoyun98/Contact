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

    fun registerChat(title: String, member: HashMap<String, String>) =
        fireStore.collection("Chat").add(
            mapOf(
                "title" to title,
                "member" to member
            )
        )
}