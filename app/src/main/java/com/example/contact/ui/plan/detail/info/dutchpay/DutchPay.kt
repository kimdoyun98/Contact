package com.example.contact.ui.plan.detail.info.dutchpay

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.adapter.plan.DutchPay.DutchPayAdapter
import com.example.contact.data.plan.Plan
import com.example.contact.databinding.ActivityDutchPayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DutchPay : AppCompatActivity() {
    private lateinit var binding: ActivityDutchPayBinding
    private val viewModel: DutchPayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDutchPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val planId = intent.getStringExtra("planId")!!
        val plan = intent.getSerializableExtra("plan") as Plan

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /**
         * init
         */
        viewModel.addDisplayName(plan.member)
        viewModel.getDutch(planId).observe(this){
            viewModel.initDutch()
            it?.forEach { document ->
                val list = document.data["member"] as HashMap<String, Int>
                viewModel.setMemberPay(list)
            }
        }

        /**
         * 추가
         */
        binding.add.setOnClickListener {
            val dutchIntent = Intent(this, DutchAdd::class.java)
            dutchIntent.putExtra("planId", planId)
            dutchIntent.putExtra("members", viewModel.displayName.value!!)
            startActivity(dutchIntent)
        }

        /**
         * 멤버 별 지출 금액
         */
        val adapter = DutchPayAdapter()
        binding.memberDutchpayRecycler.adapter = adapter
        viewModel.memberPay.observe(this){
            adapter.setData(it)

            val total = it.values.toList().sum()
            binding.total.text = total.toString()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}