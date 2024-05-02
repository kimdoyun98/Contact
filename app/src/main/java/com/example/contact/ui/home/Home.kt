package com.example.contact.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.contact.adapter.home.InvitePlanAdapter
import com.example.contact.databinding.FragmentHomeBinding
import com.example.contact.ui.home.add_plan.AddPlan
import com.example.contact.ui.home.friend.FriendManagement
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class Home : Fragment() {
    private val tag = "Home"
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var firebaseRepository: FirebaseRepository
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        /**
         * 초대 알림
         */
        val inviteAdapter = InvitePlanAdapter(viewModel, firebaseRepository)
        binding.inviteViewpager.adapter = inviteAdapter

        viewModel.inviteList.observe(viewLifecycleOwner){
            val list = mutableListOf<DocumentSnapshot>()
            it?.documents?.forEach { document ->
                list.add(document)
            }

            inviteAdapter.setInviteData(list)
        }

        viewpagerInit()

        /**
         * 일정 추가
         */
        binding.addPlanButton.setOnClickListener {
            startActivity(Intent(MyApplication.getInstance(), AddPlan::class.java))
        }

        /**
         * 친구 관리
         */
        binding.addFriendButton.setOnClickListener {
            startActivity(Intent(MyApplication.getInstance(), FriendManagement::class.java))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun viewpagerInit(){
        binding.inviteViewpager.clipToPadding = false
        binding.inviteViewpager.clipChildren = false
        binding.inviteViewpager.offscreenPageLimit = 3
        binding.inviteViewpager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.inviteViewpager.setPageTransformer(compositePageTransformer)
    }
}