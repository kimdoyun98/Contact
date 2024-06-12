package com.example.contact.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contact.databinding.RefactoringFragmentHomeBinding
import com.example.contact.ui.home.add_plan.AddPlan
import com.example.contact.ui.home.friend.FriendManagement
import com.example.contact.ui.home.notification.Notification
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {
    private lateinit var binding: RefactoringFragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var firebaseRepository: FirebaseRepository
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        /**
         * 초대 알림
         */
        binding.notification.setOnClickListener {
            val notificationIntent = Intent(MyApplication.getInstance(), Notification::class.java)
            startActivity(notificationIntent)
        }

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
    ): View {
        binding = RefactoringFragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}