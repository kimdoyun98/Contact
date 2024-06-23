package com.example.contact.ui.plan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contact.adapter.plan.PlanListAdapter
import com.example.contact.data.plan.PlanData
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
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlanListAdapter()
        binding.planRecycler.adapter = adapter

        /**
         * My Plan List
         */
        viewModel.myPlanList.observe(viewLifecycleOwner){
            val list = mutableListOf<DocumentSnapshot>()
            it?.documents?.forEach { document ->
                list.add(document)
            }
            adapter.setPlanList(list)
        }

        /**
         * Click on Item of List
         */
        adapter.setClickEvent(object : PlanClickEvent{
            override fun onPlanClickEvent(doc: DocumentSnapshot) {
                // 일정 상세로 넘어가기
                val intent = Intent(MyApplication.getInstance(), PlanDetail::class.java)
                intent.putExtra("planId", doc.id)
                val data = PlanData(
                    title = doc.data!!["title"] as String,
                    member = doc.data!!["member"] as ArrayList<String>,
                    displayNames = doc.data!!["displayNames"] as ArrayList<String>,
                    invite = doc.data!!["invite"] as ArrayList<String>,
                    date = doc.data!!["date"] as ArrayList<String>,
                    time = doc.data!!["time"] as String?
                )
                intent.putExtra("plan_data", data)
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