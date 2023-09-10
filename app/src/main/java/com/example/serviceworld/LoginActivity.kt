package com.example.serviceworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.serviceworld.fragments.CustomerLoginFragment
import com.example.serviceworld.fragments.ProviderLoginFragment
import com.example.serviceworld.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPagerL
        val tabLayout = binding.loginTabs
        val fragmentAdapter = FragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(CustomerLoginFragment(this), "Customer")
        fragmentAdapter.addFragment(ProviderLoginFragment(this), "Service provider")


        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)




    }
}