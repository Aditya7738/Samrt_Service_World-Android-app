@file:Suppress("DEPRECATION")

package com.example.serviceworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.serviceworld.databinding.ActivityBottomNavBinding
import com.example.serviceworld.fragments.FavouriteFragment
import com.example.serviceworld.fragments.FragmentContainerView
import com.example.serviceworld.fragments.HomeFragment
import com.example.serviceworld.fragments.NotificationFragment
import com.example.serviceworld.fragments.ProfileFragment

class BottomNavActivity : AppCompatActivity() {

    lateinit var binding : ActivityBottomNavBinding
    lateinit var selectedAccount: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        selectedAccount = intent.getStringExtra("AccountType").toString()

        val homeFragment = HomeFragment(this)
        val favouriteFragment = FavouriteFragment(this)
        val notificationFragment = NotificationFragment(this)
        //val profileFragment = ProfileFragment(selectedAccount)
        val profileFragmentContainer = FragmentContainerView()

        binding.bottomNav.itemIconTintList = null

        replaceFragment(homeFragment)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_icon -> replaceFragment(homeFragment)
                R.id.fav_icon -> replaceFragment(favouriteFragment)
                R.id.notify_icon -> replaceFragment(notificationFragment)
                R.id.profile_icon -> replaceFragment(profileFragmentContainer)

                else -> {

                }

            }
            true
        }



    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentBody, fragment)
        fragmentTransaction.commit()
    }
}