package com.example.contact.ui.plan.detail

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.databinding.ActivityPlanDetailBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class PlanDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBinding
    private val viewModel: PlanDetailViewModel by viewModels()
    private var planId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        planId = intent.getStringExtra("planId")!!
        viewModel.setPlanId(planId)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getPlanData().observe(this){
            binding.plan = it!!
            viewModel.getMemberDisplayName(it.member)

            if(it.date.isNotEmpty()) setTabLayout(it.date)
        }

        binding.dateButton.setOnClickListener {
            setDate()
        }
    }

    private fun setTabLayout(d: ArrayList<out String?>){
        Log.e("Date", d.toString())
        for(day in d[0]!!.toInt()..d[1]!!.toInt()){
            val year = day.toString().substring(0..3)
            val md = day.toString().substring(4..7)
            binding.tab.addTab(binding.tab.newTab().setText("$year\n$md"))
        }

        setTabItemMargin(binding.tab, 30)

        val bundle = Bundle().apply {
            putString("date", d[0])
            putString("planId", planId)
        }
        val fragment = TabFragment()
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fc_view, fragment)
        transaction.commit()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragment.setTabChange(tab!!.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setDate(){
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("기간을 선택해 주세요.")
            .build()
        dateRangePicker.show(supportFragmentManager, "date picker")
        dateRangePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it?.first ?: 0
            val start = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()
            calendar.timeInMillis = it?.second ?: 0
            val end = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()

            viewModel.setDate(start, end)

        }
    }

    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
        for(i in 0 until 3) {
            val tabs = tabLayout.getChildAt(0) as ViewGroup
            for(i in 0 until tabs.childCount) {
                val tab = tabs.getChildAt(i)
                val lp = tab.layoutParams as LinearLayout.LayoutParams
                lp.marginEnd = marginEnd
                tab.layoutParams = lp
                tabLayout.requestLayout()
            }
        }
    }
}