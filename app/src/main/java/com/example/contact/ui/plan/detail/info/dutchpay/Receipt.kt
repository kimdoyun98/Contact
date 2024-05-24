package com.example.contact.ui.plan.detail.info.dutchpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.contact.R
import com.example.contact.adapter.plan.DutchPay.ReceiptAdapter
import com.example.contact.data.plan.dutch.ReceiptData
import com.example.contact.databinding.ActivityReceiptBinding

class Receipt : AppCompatActivity() {
    private lateinit var binding: ActivityReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val receipt = intent.getSerializableExtra("dutch_item") as ArrayList<ReceiptData>
        val total = intent.getStringExtra("total")

        val adapter = ReceiptAdapter(receipt)
        binding.receiptRecycler.adapter = adapter

        binding.total.text = total
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()

        return super.onOptionsItemSelected(item)
    }
}