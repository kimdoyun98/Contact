package com.example.contact.ui.plan.detail.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.contact.R
import com.example.contact.databinding.ActivityPlanDetailInfoBinding

class PlanDetailInfo : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri: String? = intent.getStringExtra("imgUri")
        Log.e("PlanDetailInfo", uri.toString())
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.image)
    }
}