package com.example.contact.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.contact.databinding.FragmentSettingsBinding
import com.example.contact.ui.sign.Login
import com.example.contact.util.MyApplication
import com.example.contact.util.firebase.UserInfoRepository
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Settings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    @Inject
    lateinit var userInfoRepository: UserInfoRepository
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)


        binding.logout.setOnClickListener{
            userInfoRepository.fireAuth.signOut()
            /**
             * kakao
             */
            lifecycleScope.launch {
                UserApiClient.instance.logout { error ->
                    if (error == null) {
                        Toast.makeText(MyApplication.getInstance(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
                        MyApplication.prefs.setString("fb_token", null)
                    }
                }
            }
            startActivity(Intent(MyApplication.getInstance(), Login::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsBinding.inflate(layoutInflater).root

}