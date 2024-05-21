package com.example.contact.ui.plan.detail.info

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.contact.R
import com.example.contact.adapter.plan.DetailPlanInfoAdapter
import com.example.contact.adapter.plan.ImageViewpagerAdapter
import com.example.contact.databinding.ActivityPlanDetailInfoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class PlanDetailInfo : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailInfoBinding
    private val viewModel: PlanDetailInfoViewModel by viewModels()
    private lateinit var doc: DocInfo
    private lateinit var updateIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detail = intent.getSerializableExtra("detail")
        Log.e("PlanDetailInfo", detail.toString())

        setSupportActionBar(binding.materialToolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        doc = DocInfo(
            planId = intent.getStringExtra("planId")!!,
            date = intent.getStringExtra("date")!!,
            dplanId = intent.getStringExtra("dplanId")!!
        )
        viewModel.setDocInfo(doc)

        updateIntent = Intent(this, InfoUpdate::class.java).apply {
            putExtra("data", doc)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val adapter = DetailPlanInfoAdapter()
        binding.photo.adapter = adapter

        viewModel.getDetailPlan(doc.planId, doc.date, doc.dplanId).observe(this){ detailPlan ->
            val list = detailPlan?.images?.map{ Uri.parse(it) }!!
            adapter.setData(list)
            binding.time = detailPlan.time
            binding.location = detailPlan.location
            viewModel.address = detailPlan.address
            viewModel.imgList = list
        }

        adapter.setClickEvent(object : ImageClickEvent{
            override fun photoClick(position: Int) {
                val dialog = Dialog(this@PlanDetailInfo)
                dialog.setContentView(R.layout.image_big_dialog)
                dialog.show()

                val viewpager = dialog.findViewById<ViewPager2>(R.id.viewpager)
                val adapter = ImageViewpagerAdapter()
                viewpager.adapter = adapter
                adapter.setData(viewModel.imgList)

                viewpager.setCurrentItem(position, false)
            }

            override fun addClick() {
                // 갤러리로 이동 또는 imgUrl 등록
                val dialog = Dialog(this@PlanDetailInfo)
                dialog.setContentView(R.layout.photo_save_dialog)
                dialog.show()

                val image = dialog.findViewById<ImageView>(R.id.imageView)
                val clip = dialog.findViewById<Button>(R.id.clip)
                val gallery = dialog.findViewById<Button>(R.id.gallery)
                val save = dialog.findViewById<Button>(R.id.save)

                viewModel.currentPhoto.observe(this@PlanDetailInfo){
                    Glide
                        .with(this@PlanDetailInfo)
                        .load(it)
                        .centerCrop()
                        .into(image)
                }

                clip.setOnClickListener {
                    // 클립 보드 붙여넣기
                    clipBoard(dialog)
                }

                gallery.setOnClickListener {
                    // 갤러리
                    openGallery()
                }

                save.setOnClickListener {
                    //save
                    if(viewModel.currentPhoto.value != null) {
                        viewModel.imageSave()
                        dialog.cancel()
                    }
                    else Toast.makeText(this@PlanDetailInfo, "사진이 없습니다.", Toast.LENGTH_LONG).show()
                }
            }

        })

        binding.map.setOnClickListener {
            // NaverMap
        }
        binding.navi.setOnClickListener {
            // kakao Navi
            Log.e("Address", viewModel.address.toString())
            if(viewModel.address.isNullOrEmpty()){
                val snack = Snackbar.make(binding.root, "주소가 없습니다. 추가하시겠습니까?", Snackbar.LENGTH_LONG)
                snack.setAction("추가") {
                    startActivity(updateIntent)
                }
                snack.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_setting2 ->{
                startActivity(updateIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun clipBoard(dialog: Dialog){
        val clipManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if(clipManager.hasPrimaryClip()){
            val data = clipManager.primaryClip
            if(data == null){
                Toast.makeText(this, "이미지를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else{
                data.run {
                    // Gets the first item from the clipboard data.
                    val item: ClipData.Item = getItemAt(0)
                    // Tries to get the item's contents as a URI.
                    val pasteUri: Uri? = item.uri
                    if(pasteUri == null){
                        Toast.makeText(this@PlanDetailInfo, "이미지를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        viewModel.setCurrentPhoto(pasteUri)
                    }
                }
            }
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
                    viewModel.setCurrentPhoto(it)
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_plan_menu2, menu)
        return true
    }
}