package com.example.contact.ui.plan.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.contact.R
import com.example.contact.databinding.FragmentTabBinding
import com.example.contact.ui.plan.detail.add.DetailPlanAdd
import com.example.contact.util.MyApplication

class TabFragment : Fragment() {
    private val viewModel: TabViewModel by viewModels()
    private lateinit var binding: FragmentTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            val intent = Intent(MyApplication.getInstance(), DetailPlanAdd::class.java)
            intent.putExtra("date", viewModel.date.value)
            intent.putExtra("planId", viewModel.planId)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        viewModel.setDate(requireArguments().getString("date")!!)
        viewModel.planId = requireArguments().getString("planId")!!
        return binding.root
    }

    fun setTabChange(string: String){
        viewModel.setDate(string)
    }

}