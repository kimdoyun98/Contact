package com.example.contact.ui.plan.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.R
import com.example.contact.data.plan.PlanData
import com.example.contact.databinding.ActivityPlanDetailBinding
import com.example.contact.databinding.DetailPlanSettingsBsBinding
import com.example.contact.ui.plan.detail.info.dutchpay.DutchPay
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class PlanDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBinding
    private val viewModel: PlanDetailViewModel by viewModels()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var planId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * init
         */
        planId = intent.getStringExtra("planId")!!
        viewModel.setPlanId(planId)
        viewModel.setPlan(intent.getSerializableExtra("plan_data") as PlanData)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.title = viewModel.getPlan()?.title

        /**
         * Plan Data
         */
        viewModel.getPlanData().observe(this){
            viewModel.getMemberDisplayName(it!!.member)

            viewModel.setPlan(it)
            if(it.date.isNotEmpty() && binding.tab.tabCount == 0) setTabLayout(it.date)
        }

        /**
         * 일정이 없다면 일정 등록
         */
        binding.dateButton.setOnClickListener {
            setDate()
        }
    }

    /**
     * 일정이 없을 경우
     */
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


    /**
     * Tab init
     */
    private fun setTabLayout(d: ArrayList<out String?>){
        for(day in d[0]!!.toInt()..d[1]!!.toInt()){
            val md = day.toString().substring(4..7)
            val tabName = StringBuilder(md).apply {
                insert(2, "월 ")
                append("일")
            }.toString()

            binding.tab.addTab(binding.tab.newTab().setText(tabName))
            viewModel.setDateMap(tabName, "$day")
        }

        setTabItemMargin(binding.tab, 30)

        val bundle = Bundle().apply {
            putString("date", d[0])
        }
        val fragment = TabFragment()
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fc_view, fragment)
        transaction.commit()

        // Tab Click
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val text = tab!!.text.toString()
                fragment.setTabChange(viewModel.getDateMapValue(text))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_setting -> {
                bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
                val bsBinding = DetailPlanSettingsBsBinding.inflate(LayoutInflater.from(this))
                initSettings(bsBinding)
                bottomSheetDialog.setContentView(bsBinding.root)
                bottomSheetDialog.show()
            }
            R.id.menu_money ->{
                val intent = Intent(this, DutchPay::class.java)
                intent.putExtra("planId", planId)
                intent.putExtra("plan", viewModel.getPlan())
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_plan_menu, menu)
        return true
    }

    private fun initSettings(bsBinding: DetailPlanSettingsBsBinding){
        bsBinding.updateTitle.setOnClickListener {

            bottomSheetDialog.dismiss()
        }

        bsBinding.inviteMember.setOnClickListener {

            bottomSheetDialog.dismiss()
        }

        bsBinding.managePlan.setOnClickListener {

            bottomSheetDialog.dismiss()
        }

        bsBinding.checkout.setOnClickListener {

            bottomSheetDialog.dismiss()
        }
    }
}