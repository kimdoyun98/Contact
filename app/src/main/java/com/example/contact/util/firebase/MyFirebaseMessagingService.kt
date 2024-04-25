package com.example.contact.util.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.contact.R
import com.example.contact.util.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {

    @Inject lateinit var firebaseRepository: FirebaseRepository

    private val CHANNEL_ID = "FCM_ID"
    private val CHANNEL_NAME = "FCM"

    override fun onNewToken(token: String) {
        Log.e("MyFirebaseMessagingService", "token: $token")
        super.onNewToken(token)
        // 내 uid 구하기
        val user = firebaseRepository.fireAuth.currentUser

        // 서버로 토큰 저장
        firebaseRepository.getUserInfo(user!!.uid)
            .collection("CloudMessaging").document("Token")
            .set("token" to token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.takeIf { it.data.isNotEmpty() }?.apply {
            //FCM이 왔을 때 처리해 줄 작업 >> Notification
            val context = MyApplication.getInstance()
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            notificationManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.group)
                .setContentTitle(this.data["title"])
                .setContentText(this.data["body"])
                .setAutoCancel(false)
                .setShowWhen(true)
                .setColor(ContextCompat.getColor(context, R.color.purple_200))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(1, builder)
        }

    }
}