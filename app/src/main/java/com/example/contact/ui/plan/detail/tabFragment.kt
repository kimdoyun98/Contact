package com.example.contact.ui.plan.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.contact.adapter.plan.DetailPlanAdapter
import com.example.contact.data.plan.DetailPlan
import com.example.contact.databinding.FragmentTabBinding
import com.example.contact.ui.plan.detail.add.DetailPlanAdd
import com.example.contact.ui.plan.detail.info.PlanDetailInfo
import com.example.contact.util.MyApplication
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TabFragment : Fragment() {
    private val viewModel: PlanDetailViewModel by activityViewModels()
    private lateinit var binding: FragmentTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDate(requireArguments().getString("date")!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * 일정 추가
         */
        binding.addButton.setOnClickListener {
            val intent = Intent(MyApplication.getInstance(), DetailPlanAdd::class.java)
            intent.putExtra("date", viewModel.date.value)
            intent.putExtra("planId", viewModel.getPlanId())
            startActivity(intent)
        }

        val adapter = DetailPlanAdapter(this)
        binding.detailPlanRecycler.adapter = adapter


        /**
         * Tab 클릭 시 변환
         */
        viewModel.date.observe(viewLifecycleOwner){ date ->
            Log.e("date", date)

            viewModel.getDetailPlan().observe(viewLifecycleOwner){
                val list = mutableListOf<DocumentSnapshot>()
                it?.documents?.forEach { document ->
                    list.add(document)
                }
                adapter.setData(list)
            }
        }

        /**
         * 일정 클릭
         */
        adapter.setOnClick(object : PlanDetailClick{
            override fun planDetailClick(id: String, detailPlan: DetailPlan) {
                val intent = Intent(MyApplication.getInstance(), PlanDetailInfo::class.java)
                intent.putExtra("detail", detailPlan)
                intent.putExtra("dplanId", id)
                intent.putExtra("planId", viewModel.getPlanId())
                intent.putExtra("date", viewModel.date.value)
                startActivity(intent)
            }

            override fun planDetailLongClick(docId: String) {
                AlertDialog.Builder(requireActivity())
                    .setTitle("삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        viewModel.deleteDetailPlan(docId)
                    }
                    .setNegativeButton("취소") { _, _ -> }
                    .create()
                    .show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun setTabChange(string: String){
        viewModel.setDate(string)
    }

}