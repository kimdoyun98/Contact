package com.example.contact.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.contact.R
import com.example.contact.adapter.home.InvitePlanAdapter
import com.example.contact.databinding.FragmentPlanBinding
import com.example.contact.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Plan : Fragment() {
    private lateinit var binding: FragmentPlanBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPlanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}