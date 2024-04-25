package com.example.contact.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.ui.sign.Login
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Splash : AppCompatActivity() {
    @Inject lateinit var firebaseRepository: FirebaseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkToken()
    }

    private fun checkToken(){
        val loginIntent = Intent(this, Login::class.java)
        val mainIntent = Intent(this, MainActivity::class.java)

        val user = firebaseRepository.fireAuth.currentUser
        if (user != null) startActivity(mainIntent)
        else startActivity(loginIntent)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}