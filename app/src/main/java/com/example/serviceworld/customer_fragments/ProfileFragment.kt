package com.example.serviceworld.customer_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.serviceworld.CustomerBottomNavActivity
import com.example.serviceworld.UpdateProfile
import com.example.serviceworld.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment(customerBottomNavActivity: CustomerBottomNavActivity) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    val context = customerBottomNavActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()



        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }


        Log.d("UID----------------", uid)


            db.collection("users").document("Customer").collection("profile_data")
                .document(uid).get().addOnSuccessListener { document ->
                    if (document != null) {


                        binding.emailId.text = document.getString("email")
                        binding.location.text = document.getString("location")
                        binding.phoneNo.text = document.getString("phone")
                        binding.name.text = document.getString("name")

                    }
                }



        //binding.profileImage.setImageResource()

        binding.updateBtn.setOnClickListener {

            val location = binding.location.text.toString()
            val phoneNo = binding.phoneNo.text.toString()
            val name = binding.name.text.toString()

            val intent = Intent(context, UpdateProfile::class.java)

            intent.putExtra("location", location)
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("name", name)

            startActivity(intent)
        }

        return binding.root
    }

}