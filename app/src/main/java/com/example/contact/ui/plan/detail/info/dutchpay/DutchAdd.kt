package com.example.contact.ui.plan.detail.info.dutchpay

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contact.R
import com.example.contact.adapter.plan.DutchPay.DutchAddMemberAdapter
import com.example.contact.adapter.plan.DutchPay.ManualDutchAdapter
import com.example.contact.data.plan.Plan
import com.example.contact.databinding.ActivityDutchAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DutchAdd : AppCompatActivity() {
    private lateinit var binding: ActivityDutchAddBinding
    private val viewModel: DutchAddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDutchAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val plan = intent.getSerializableExtra("plan") as Plan
        val planId = intent.getStringExtra("planId")!!

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            viewModel.addDisplayName(plan.member, planId)
        }

        val adapter = DutchAddMemberAdapter(viewModel, this)
        binding.memberRecycler.adapter = adapter

        val notAutoAdapter = ManualDutchAdapter(viewModel, this)
        binding.manualDutch.adapter = notAutoAdapter

        viewModel.displayName.observe(this){
            adapter.setData(it)
        }


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.auto -> {
                    viewModel.changeAuto(true)
                }
                R.id.not_auto -> {
                    viewModel.changeAuto(false)
                    viewModel.checkCurrentFriend.observe(this@DutchAdd){
                        notAutoAdapter.setData(it)
                    }
                }
            }
        }

        viewModel.saveStatus.observe(this){
            if(it) finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> finish()
            R.id.menu_check -> {
                viewModel.save(
                    binding.memo.text.toString(),
                    if(viewModel.autoStatus.value!!) binding.autoDutch.text.toString().toInt() else 0
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_check_menu, menu)
        return true
    }
}