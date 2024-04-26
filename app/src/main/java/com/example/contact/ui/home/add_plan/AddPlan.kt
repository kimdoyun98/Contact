package com.example.contact.ui.home.add_plan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.contact.R
import com.example.contact.databinding.ActivityAddPlanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPlan : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlanBinding
    private val viewModel: AddPlanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.addPlanToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addFriendButton.setOnClickListener {
            val intent = Intent(this, AddFriendToPlan::class.java)
            //startActivity(Intent(this, AddFriendToPlan::class.java))
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                //binding.locationAdd.text = data.getStringExtra("address")
                val test = data.getStringArrayListExtra("friendList")
                Log.e("AddPlan", test.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        return super.onOptionsItemSelected(item)
    }
}