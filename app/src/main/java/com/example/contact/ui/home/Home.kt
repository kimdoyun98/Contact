package com.example.contact.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contact.adapter.home.HomePlanAdapter
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

        /**
         * 나의 일정
         */
        val adapter = HomePlanAdapter(viewModel)
        binding.myPlan.adapter = adapter

        viewModel.myPlanList.observe(viewLifecycleOwner){
            val list = mutableListOf<Pair<String, String>>()
            it?.documents?.forEach { document ->
                val dateList = document.data!!["date"] as ArrayList<String>
                if(dateList.isNotEmpty() && viewModel.checkLastDay(dateList[0])){
                    list.add(document.data!!["title"] as String to dateList[0])
                }
            }
            list.sortBy {  data ->
                data.second
            }

            if(list.isEmpty()) binding.noPlan.visibility = View.VISIBLE
            else binding.noPlan.visibility = View.GONE

            adapter.setPlanList(list)
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