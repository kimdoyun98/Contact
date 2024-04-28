package com.example.contact.adapter.friend_manage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import com.example.contact.data.user.UserInfo
import com.example.contact.databinding.FriendListBsBinding
import com.example.contact.databinding.FriendListItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import io.github.horaciocome1.fireflow.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendListAdapter: RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    private lateinit var binding: FriendListItemBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var context: Context
    private var uidList = mutableListOf<String>()

    fun setUidList(list: MutableList<String>?, context: Context){
        if (list != null) uidList = list
        this.context = context
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(position: Int){
            CoroutineScope(Dispatchers.Main).launch {
                FirebaseFirestore.getInstance().collection("Users").document(uidList[position])
                    .asFlow<UserInfo>().collect{
                        binding.displayName = it?.displayName
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        binding.moreButton.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTheme)

            val bsBinding = FriendListBsBinding.inflate(LayoutInflater.from(context))
            bsBinding.bsDelete.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                builder.setTitle("삭제")
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("예"){ _, _ ->
                        //TODO friend DB에서 uid 삭제
                        // 나의 DB
                        val myFriend = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
                            .collection("Friend").document("friend")

                        val userFriend = Firebase.firestore.collection("Users").document(uidList[position])
                            .collection("Friend").document("friend")

                        myFriend.get().addOnSuccessListener {
                                val list = it.data!!["friend"] as ArrayList<String>
                                val index = list.indexOf(uidList[position])
                                list.removeAt(index)

                                myFriend.set(hashMapOf("friend" to list))
                            }

                        // 친구 DB
                        userFriend.get().addOnSuccessListener {
                            val list = it.data!!["friend"] as ArrayList<String>
                            val index = list.indexOf(Firebase.auth.currentUser!!.uid)
                            list.removeAt(index)

                            myFriend.set(hashMapOf("friend" to list))
                        }

                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    .setNegativeButton("아니오"){ dialog , _ ->
                        dialog?.dismiss()
                    }
                    .create()
                    .show()

                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(bsBinding.root)
            bottomSheetDialog.show()
        }
    }

    override fun getItemCount(): Int = uidList.size

}