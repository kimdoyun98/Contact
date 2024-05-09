package com.example.contact.adapter.plan

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import com.example.contact.R
import com.example.contact.ui.plan.detail.add.DetailPlanAddViewModel
import com.example.contact.ui.plan.detail.add.GalleryClickEvent

class DetailPlanAddAdapter constructor(
    private val viewModel: DetailPlanAddViewModel,
    private val context: Context
): PhotoAdapter() {
    private var galleryClickEvent: GalleryClickEvent? = null
    fun setData(list: MutableList<Uri>){
        this.imgUrl = list
        notifyDataSetChanged()
    }

    fun setGalleryClickEvent(event: GalleryClickEvent){
        galleryClickEvent = event
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        binding.add.setOnClickListener {
            // 갤러리로 이동 또는 imgUrl 등록
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.photo_upload_type_dialog)
            dialog.show()

            val clip = dialog.findViewById<Button>(R.id.clip)
            val gallery = dialog.findViewById<Button>(R.id.gallery)

            clip.setOnClickListener {
                // 클립 보드 붙여넣기
                clipBoard(dialog)
            }

            gallery.setOnClickListener {
                // 갤러리
                galleryClickEvent?.clickGallery()
            }
        }
    }

    private fun clipBoard(dialog: Dialog){
        val clipManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if(clipManager.hasPrimaryClip()){
            val data = clipManager.primaryClip
            if(data == null){
                Toast.makeText(context, "이미지를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
            }
            else{
                data.run {
                    // Gets the first item from the clipboard data.
                    val item: ClipData.Item = getItemAt(0)
                    // Tries to get the item's contents as a URI.
                    val pasteUri: Uri? = item.uri
                    if(pasteUri == null){
                        Toast.makeText(context, "이미지를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        viewModel.addImgUri(pasteUri)
                        dialog.cancel()
                    }
                }
            }
        }
    }
}