package com.example.contact.ui.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contact.BuildConfig
import com.example.contact.databinding.ActivityLoginBinding
import com.example.contact.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: KakaoAuthViewModel by viewModels()
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoViewModel = viewModel
        binding.lifecycleOwner = this

        binding.kakaoLogin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.kakaoLogin(this@Login)

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


        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //기본 로그인 방식 사용
            .requestIdToken(BuildConfig.web_client_id)
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(this, options)

        binding.googleLogin.setOnClickListener {
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            viewModel.startLoading()
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuthWithGoogle(credential)

            } catch (e: ApiException){
                e.printStackTrace()
            }

        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        viewModel.addFirebaseDB()
                        Log.e("firebaseAuthWithGoogle", "finish ViewModel")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else {
                        // 오류가 난 경우!
                        Log.e("firebaseAuthWithGoogle", "${task.exception?.message}")
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                })
    }
}