package com.example.contact.ui.plan.detail.add

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contact.R
import com.example.contact.adapter.plan.DetailPlanAddAdapter
import com.example.contact.databinding.ActivityDetailPlanAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class DetailPlanAdd : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlanAddBinding
    private val viewModel: DetailPlanAddViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlanAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.dpaToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.setDate(
            intent.getStringExtra("date")!!,
            intent.getStringExtra("planId")!!
        )

        val adapter = DetailPlanAddAdapter(viewModel, this)
        binding.photoRecycler.adapter = adapter

        viewModel.imgUri.observe(this){
            Log.e("uri", it.toString())
            adapter.setData(it)
        }

        adapter.setGalleryClickEvent(object : GalleryClickEvent{
            override fun clickGallery() {
                openGallery()
            }
        })

        lifecycleScope.launch {
            viewModel.registerStatus.collect{
                if(it) finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()

        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_check ->{
                //TODO
                var time: List<String>? = null
                val cal = Calendar.getInstance()
                val arr = arrayOf(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                time = arr.map { "%02d".format(it) }
                val location = binding.location.text.toString()
                val address = binding.address.text.toString()

                viewModel.register(time, location, address)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_check_menu, menu)
        return true
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            }
        }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    viewModel.addImgUri(it)
                }
            }
        }
}