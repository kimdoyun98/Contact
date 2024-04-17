package com.example.contact.ui.sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contact.databinding.ActivityLoginBinding
import com.example.contact.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: KakaoAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoViewModel = viewModel
        binding.lifecycleOwner = this

        binding.kakaoLogin.setOnClickListener {
            binding.kakaoLogin.setOnClickListener {
                viewModel.kakaoLogin(this@Login)

            }

            lifecycleScope.launch {
                viewModel.logInStatus.collect{
                    if(it){
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        //TODO 로그인 실패
                        viewModel.onError()
                    }
                }
            }
        }
    }
}