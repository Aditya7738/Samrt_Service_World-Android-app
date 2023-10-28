@file:Suppress("DEPRECATION")

package com.example.serviceworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.serviceworld.databinding.ActivityBottomNavBinding
import com.example.serviceworld.fragments.FavouriteFragment
import com.example.serviceworld.fragments.HomeFragment
import com.example.serviceworld.fragments.RequestedServicesFragment
import com.example.serviceworld.fragments.ProfileFragment

class BottomNavActivity : AppCompatActivity() {

    var fromProfile: Boolean = false
    lateinit var binding : ActivityBottomNavBinding
    lateinit var selectedAccount: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        selectedAccount = intent.getStringExtra("AccountType").toString()

        Log.d("BOTTOM", selectedAccount)

        val homeFragment = HomeFragment(this)
        val favouriteFragment = FavouriteFragment(this)
        val requestedServicesFragment = RequestedServicesFragment(this)
        val profileFragment = ProfileFragment(selectedAccount)
        //val profileFragmentContainer = FragmentContainerView()

        binding.bottomNav.itemIconTintList = null

        val intent = intent
        if(intent != null){
            fromProfile = intent.getBooleanExtra("fromProfile", false)
            val accountType = intent.getStringExtra("accountType").toString()

            Log.d("RETURN_ACCOUNT", accountType)
            if(fromProfile){
                binding.bottomNav.selectedItemId = R.id.profile_icon
                replaceFragment(ProfileFragment(accountType))
            }else{
                replaceFragment(homeFragment)
            }
        }


        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_icon -> replaceFragment(homeFragment)
                R.id.fav_icon -> replaceFragment(favouriteFragment)
                R.id.notify_icon -> replaceFragment(requestedServicesFragment)
                R.id.profile_icon -> replaceFragment(profileFragment)

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