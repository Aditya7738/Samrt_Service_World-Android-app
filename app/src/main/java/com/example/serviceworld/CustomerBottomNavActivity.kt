@file:Suppress("DEPRECATION")

package com.example.serviceworld

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.serviceworld.customer_fragments.FavouriteFragment
import com.example.serviceworld.customer_fragments.HomeFragment
import com.example.serviceworld.customer_fragments.ProfileFragment
import com.example.serviceworld.customer_fragments.RequestedServicesFragment
import com.example.serviceworld.databinding.ActivityCustomerBottomNavBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class CustomerBottomNavActivity : AppCompatActivity() {

    private var fromProfile: Boolean = false
    lateinit var binding: ActivityCustomerBottomNavBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val intent = intent

        auth = FirebaseAuth.getInstance()

        val homeFragment = HomeFragment(this)
        val favouriteFragment = FavouriteFragment(this)
        val requestedServicesFragment = RequestedServicesFragment(this)
        val profileFragment = ProfileFragment(this)
        //val profileFragmentContainer = FragmentContainerView()

        binding.bottomNav.itemIconTintList = null

        if (intent != null) {
            fromProfile = intent.getBooleanExtra("fromProfile", false)

            if (fromProfile) {

                binding.bottomNav.selectedItemId = R.id.profile_icon

                replaceFragment(profileFragment)
            } else {
                replaceFragment(homeFragment)
            }
        }


        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_icon -> replaceFragment(homeFragment)
                R.id.fav_icon -> replaceFragment(favouriteFragment)
                R.id.request_icon -> replaceFragment(requestedServicesFragment)
                R.id.profile_icon -> replaceFragment(profileFragment)

                else -> {

                }

            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentBody, fragment)
        fragmentTransaction.commit()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        return if (itemId == R.id.logout_icon) {

            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener{task->
                if(task.isSuccessful){
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}