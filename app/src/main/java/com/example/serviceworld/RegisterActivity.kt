package com.example.serviceworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.serviceworld.Fragments.SProviderFragment
import com.example.serviceworld.Fragments.UserFragment
import com.example.serviceworld.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.registerTabs
        val fragmentAdapter = FragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(UserFragment(this), "User")
        fragmentAdapter.addFragment(SProviderFragment(this), "Service provider")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

    }



}