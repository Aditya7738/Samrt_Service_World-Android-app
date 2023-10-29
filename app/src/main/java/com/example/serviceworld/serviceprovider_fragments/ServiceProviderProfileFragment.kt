package com.example.serviceworld.serviceprovider_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.serviceworld.ServiceProviderBottomNavActivity
import com.example.serviceworld.ServiceProviderUpdateProfileActivity
import com.example.serviceworld.databinding.FragmentServiceProviderProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ServiceProviderProfileFragment(serviceProviderBottomNavActivity: ServiceProviderBottomNavActivity) : Fragment() {

    val context = serviceProviderBottomNavActivity
    lateinit var binding: FragmentServiceProviderProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentServiceProviderProfileBinding.inflate(layoutInflater, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        db.collection("users").document("Service Provider").collection("profile_data")
            .document(uid).get().addOnSuccessListener { document ->
                if (document != null) {

                    binding.emailId.text = document.getString("email")
                    binding.location.text = document.getString("location")
                    binding.phoneNo.text = document.getString("phone")
                    binding.name.text = document.getString("name")
                    binding.roleName.text = document.getString("serviceName")

                    Log.d("GETDATA", document.getString("serviceName").toString())


                }
            }

        binding.updateBtn.setOnClickListener {

            val location = binding.location.text.toString()
            val phoneNo = binding.phoneNo.text.toString()
            val name = binding.name.text.toString()
            val roleName = binding.roleName.text.toString()

            val intent = Intent(context, ServiceProviderUpdateProfileActivity::class.java)
            intent.putExtra("serviceName", roleName)
            intent.putExtra("location", location)
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("name", name)

            Log.d("ROLENAME", roleName)


            startActivity(intent)
        }

        return binding.root
    }
}