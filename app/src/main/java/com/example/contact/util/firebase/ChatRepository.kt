package com.example.contact.util.firebase

import androidx.lifecycle.asLiveData
import com.example.contact.data.chat.ChatData
import io.github.horaciocome1.fireflow.asFlow
import io.github.horaciocome1.fireflow.snapshotAsFlow

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

    fun registerChat(title: String, member: HashMap<String, String>, member2: ArrayList<String>) =
        fireStore.collection("Chat").add(
            mapOf(
                "title" to title,
                "member" to member,
                "member2" to member2
            )
        )

    fun getChatList(uid: String) = fireStore.collection("Chat").whereArrayContains("member2", uid).snapshotAsFlow().asLiveData()

    fun getChatInfo(doc: String) = fireStore.collection("Chat").document(doc).asFlow<ChatData>().asLiveData()

    fun getChatMessage(doc: String) =
        fireStore.collection("Chat").document(doc)
            .collection("Message").snapshotAsFlow().asLiveData()
}