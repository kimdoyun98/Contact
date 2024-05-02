package com.example.contact.ui.plan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.contact.R
import com.example.contact.adapter.home.InvitePlanAdapter
import com.example.contact.adapter.plan.PlanListAdapter
import com.example.contact.data.plan.PlanViewModel
import com.example.contact.databinding.FragmentPlanBinding
import com.example.contact.ui.home.HomeViewModel
import com.example.contact.ui.plan.detail.PlanDetail
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Plan : Fragment() {
    private lateinit var binding: FragmentPlanBinding
    private val viewModel: PlanViewModel by viewModels()
    @Inject lateinit var firebaseRepository: FirebaseRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlanListAdapter(firebaseRepository)
        binding.planRecycler.adapter = adapter

        viewModel.myPlanList.observe(viewLifecycleOwner){
            val list = mutableListOf<DocumentSnapshot>()
            it?.documents?.forEach { document ->
                list.add(document)
            }
            adapter.setPlanList(list)
        }

        adapter.setClickEvent(object : PlanClickEvent{
            override fun onPlanClickEvent(planId: String) {
                // 일정 상세로 넘어가기
                val intent = Intent(MyApplication.getInstance(), PlanDetail::class.java)
                intent.putExtra("planId", planId)
                startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPlanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}