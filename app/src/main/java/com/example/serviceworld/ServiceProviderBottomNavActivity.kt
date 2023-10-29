package com.example.serviceworld

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.serviceworld.databinding.ActivityServiceProviderBottomNavBinding
import com.example.serviceworld.serviceprovider_fragments.NotificationFragment
import com.example.serviceworld.serviceprovider_fragments.ServiceProviderProfileFragment
import com.google.firebase.auth.FirebaseAuth

class ServiceProviderBottomNavActivity : AppCompatActivity() {

    lateinit var binding: ActivityServiceProviderBottomNavBinding
    private var fromProfile: Boolean = false

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServiceProviderBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()

        val intent = intent


        val notificationFragment = NotificationFragment(this)
        val profileFragment = ServiceProviderProfileFragment(this)

        binding.bottomNav.itemIconTintList = null


        if (intent != null) {
            fromProfile = intent.getBooleanExtra("fromProfile", false)


            if (fromProfile) {

                binding.bottomNav.selectedItemId = R.id.profile_icon
                replaceFragment(profileFragment)
            } else {
                replaceFragment(notificationFragment)
            }
        }


        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.notify_icon -> replaceFragment(notificationFragment)
                R.id.profile_icon -> replaceFragment(profileFragment)

                else -> {

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
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

        return if(itemId == R.id.logout_icon){
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            true
        }else{
            super.onOptionsItemSelected(item)
        }
    }

}