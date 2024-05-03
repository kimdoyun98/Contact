package com.example.contact.ui.plan.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.contact.R
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.ActivityPlanDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlanDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBinding
    private val viewModel: PlanDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val planId = intent.getStringExtra("planId")!!
        viewModel.setPlanId(planId)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getPlanData().observe(this){
            binding.plan = it!!
            viewModel.getMemberDisplayName(it.member)
        }
    }
}