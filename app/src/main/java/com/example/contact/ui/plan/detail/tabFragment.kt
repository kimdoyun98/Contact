package com.example.contact.ui.plan.detail

import android.R.attr.fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.example.contact.adapter.plan.DetailPlanAdapter
import com.example.contact.databinding.FragmentTabBinding
import com.example.contact.ui.plan.detail.add.DetailPlanAdd
import com.example.contact.ui.plan.detail.info.PlanDetailInfo
import com.example.contact.util.MyApplication
import com.google.firebase.firestore.DocumentSnapshot
import com.gun0912.tedpermission.provider.TedPermissionProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TabFragment : Fragment() {
    private val viewModel: TabViewModel by viewModels()
    private lateinit var binding: FragmentTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.addButton.setOnClickListener {
            val intent = Intent(MyApplication.getInstance(), DetailPlanAdd::class.java)
            intent.putExtra("date", viewModel.date.value)
            intent.putExtra("planId", viewModel.planId)
            startActivity(intent)
        }

        val adapter = DetailPlanAdapter(this)
        binding.detailPlanRecycler.adapter = adapter

        viewModel.date.observe(viewLifecycleOwner){ date ->
            Log.e("date", date)

            viewModel.getDetailPlan().observe(viewLifecycleOwner){
                Log.e("getDetailPlan", it.toString())
                val list = mutableListOf<DocumentSnapshot>()
                it?.documents?.forEach { document ->
                    list.add(document)
                }
                Log.e("getDetailPlan list", list.toString())
                adapter.setData(list)
            }
        }
        
        adapter.setOnClick(object : PlanDetailClick{
            override fun planDetailClick(uri: String) {
                val intent = Intent(MyApplication.getInstance(), PlanDetailInfo::class.java)
                intent.putExtra("imgUri", uri)
                startActivity(intent)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        viewModel.setDate(requireArguments().getString("date")!!)
        viewModel.planId = requireArguments().getString("planId")!!
        return binding.root
    }

    fun setTabChange(string: String){
        viewModel.setDate(string)
    }

}