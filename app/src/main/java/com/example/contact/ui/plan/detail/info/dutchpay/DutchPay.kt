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

        val planId = intent.getStringExtra("planId")
        val plan = intent.getSerializableExtra("plan") as Plan

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.add.setOnClickListener {
            val dutchIntent = Intent(this, DutchAdd::class.java)
            dutchIntent.putExtra("planId", planId)
            dutchIntent.putExtra("plan", plan)
            startActivity(dutchIntent)
        }


        val adapter = DutchPayAdapter()
        binding.memberDutchpayRecycler.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}