package com.example.contact.ui.home.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.contact.R
import com.example.contact.adapter.home.InvitePlanAdapter
import com.example.contact.databinding.ActivityNotificationBinding
import com.example.contact.ui.home.HomeViewModel
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class Notification : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: HomeViewModel by viewModels()
    @Inject lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val inviteAdapter = InvitePlanAdapter(viewModel, firebaseRepository)
        binding.notificationRecycler.adapter = inviteAdapter

        viewModel.inviteList.observe(this){
            val list = mutableListOf<DocumentSnapshot>()
            it?.documents?.forEach { document ->
                list.add(document)
            }

            inviteAdapter.setInviteData(list)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}