package com.example.contact.ui.plan.detail.info

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.contact.R
import com.example.contact.databinding.ActivityInfoUpdateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityInfoUpdateBinding
    private val viewModel: InfoUpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this

        setSupportActionBar(binding.updateToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val doc: DocInfo = intent.getSerializableExtra("data") as DocInfo
        viewModel.setDocInfo(doc)

        viewModel.getDetailPlan(doc.planId, doc.date, doc.dplanId).observe(this){ detailPlan ->
            binding.info = detailPlan
        }

        binding.update.setOnClickListener {
            viewModel.update(
                binding.time.text.toString(),
                binding.location.text.toString(),
                binding.address.text.toString()
            )
        }

        viewModel.status.observe(this){
            if(it) finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()

        return super.onOptionsItemSelected(item)
    }
}