package com.example.contact.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact.R
import com.example.contact.databinding.FragmentHomeBinding
import com.example.contact.ui.home.add_plan.AddPlan
import com.example.contact.ui.home.friend.FriendManagement
import com.example.contact.util.MyApplication
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.auth

class Home : Fragment() {
    private val tag = "Home"
    private lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        /**
         * 일정 추가
         */
        binding.addPlanButton.setOnClickListener {
            startActivity(Intent(MyApplication.getInstance(), AddPlan::class.java))
        }

        /**
         * 친구 관리
         */
        binding.addFriendButton.setOnClickListener {
            startActivity(Intent(MyApplication.getInstance(), FriendManagement::class.java))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(layoutInflater).root
}