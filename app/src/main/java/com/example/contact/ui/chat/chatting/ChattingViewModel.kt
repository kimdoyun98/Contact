package com.example.contact.ui.chat.chatting

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact.R
import com.example.contact.data.chat.ChatData
import com.example.contact.data.chat.ChattingData
import com.example.contact.util.firebase.ChatRepository
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chatRepository: ChatRepository
): ViewModel() {
    private var docId: String? = null
    private lateinit var chatData: ChatData
    private val myUid = chatRepository.fireAuth.currentUser?.uid!!
    private val myName = chatRepository.fireAuth.currentUser?.displayName!!

    fun setDocID(id: String){
        docId = id
        viewModelScope.launch {
            chatRepository.getChatInfo(id).collect{
                chatData = it!!
            }
        }
    }

    fun getChatInfo(doc: String) = chatRepository.getChatInfo(doc)

    fun getChatMessage(doc: String): LiveData<QuerySnapshot?> = chatRepository.getChatMessage(doc)

    fun setChatMessage(doc: String, message: String) =
        chatRepository.setChatMessage(
            doc = doc,
            date = getCurrentTime(),
            message = message,
            author = hashMapOf(myUid to myName)
        )

    private fun getCurrentTime(): String{
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return dateFormat.format(date)
    }
}

object DataBindingAdapterUtil {
    private val chatRepository = ChatRepository()
    private val myUid = chatRepository.fireAuth.currentUser?.uid
    @JvmStatic
    @BindingAdapter("author_layout")
    fun layout(layout: LinearLayout, data: ChattingData){
        if(data.author.containsKey(myUid)){
            layout.gravity = Gravity.END
        }
        else {
            layout.gravity = Gravity.START
        }
    }
    @JvmStatic
    @BindingAdapter("author_visibility")
    fun visibility(view: TextView, data: ChattingData){
        if(data.author.containsKey(myUid)){
            view.visibility = View.INVISIBLE
        }
        else {
            view.visibility = View.VISIBLE
            view.text = data.author[myUid]
        }
    }
    @JvmStatic
    @BindingAdapter("message_settings")
    fun background(view: TextView, data: ChattingData){
        if(data.author.containsKey(myUid)){
            view.setBackgroundResource(R.drawable.my_message)
        }
        else {
            view.setBackgroundResource(R.drawable.left_message)
        }
    }
}