package com.example.contact.ui.home.add_plan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contact.adapter.add_plan.AddPlanFriendListAdapter
import com.example.contact.databinding.ActivityAddPlanBinding
import com.example.contact.util.firebase.FirebaseRepository
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AddPlan : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlanBinding
    private val viewModel: AddPlanViewModel by viewModels()
    @Inject lateinit var firebaseRepository: FirebaseRepository

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPlanViewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.addPlanToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addFriendButton.setOnClickListener {
            val intent = Intent(this, AddFriendToPlan::class.java)
            startActivityForResult(intent, 1)
        }

        val friendListAdapter = AddPlanFriendListAdapter(viewModel, firebaseRepository)
        binding.friendRecyclerView.adapter = friendListAdapter

        viewModel.friendList.observe(this){
            friendListAdapter.setFriendList(it)
        }

        /**
         * Calendar
         */
        binding.calendar.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("기간을 선택해 주세요.")
                .build()
            dateRangePicker.show(supportFragmentManager, "date picker")
            dateRangePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it?.first ?: 0
                binding.startDateText.text = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()

                calendar.timeInMillis = it?.second ?: 0
                binding.endDateText.text = SimpleDateFormat("yyyyMMdd").format(calendar.time).toString()

            }
        }

        binding.dateCheckbox.setOnClickListener {
            viewModel.setDateCheckBox(binding.dateCheckbox.isChecked)
        }

        binding.timeCheckbox.setOnClickListener {
            viewModel.setTimeCheckBox(binding.timeCheckbox.isChecked)
        }

        /**
         * Register
         */
        binding.addButton.setOnClickListener {
            val title = binding.title.text.toString()
            var startDate: String? = null
            var endDate: String? = null
            var time: List<String>? = null
            if(!binding.dateCheckbox.isChecked){
                startDate = binding.startDateText.text.toString()
                endDate = binding.endDateText.text.toString()
            }

            if(!binding.timeCheckbox.isChecked){
                val cal = Calendar.getInstance()
                val arr = arrayOf(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                time = arr.map { "%02d".format(it) }
            }

            if(startDate.isNullOrEmpty() && !binding.dateCheckbox.isChecked) {
                Toast.makeText(this, "날짜를 정해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(title.isEmpty()){
                Toast.makeText(this, "제목을 입력해주세요..", Toast.LENGTH_SHORT).show()
            }
            else viewModel.registerButton(
                title,
                startDate,
                endDate,
                time
            )
        }

        lifecycleScope.launch {
            viewModel.registerStatus.collect{
                if(it) finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                val list = data.getStringArrayListExtra("friendList")!!
                viewModel.setFriendList(list)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}