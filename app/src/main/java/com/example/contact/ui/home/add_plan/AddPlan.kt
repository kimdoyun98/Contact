package com.example.contact.ui.home.add_plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contact.R
import com.example.contact.databinding.ActivityAddPlanBinding

class AddPlan : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.addPlanToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}