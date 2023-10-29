package com.example.serviceworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceworld.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfile : AppCompatActivity() {

    lateinit var binding: ActivityUpdateProfileBinding
    lateinit var db: FirebaseFirestore

    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        binding.nameTxt.hint = intent.getStringExtra("name")
        binding.locationTxt.hint = intent.getStringExtra("location")
        binding.phoneNoTxt.hint = intent.getStringExtra("phoneNo")


        binding.saveBtn.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            val location = binding.locationTxt.text.toString()
            val phoneNo = binding.phoneNoTxt.text.toString()


            Log.d("UPDATEDATA", binding.nameTxt.text.toString())

            if (name.isEmpty()) {
                binding.nameTxt.error = "Name field is empty"
                Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show()
            }

            if (location.isEmpty()) {
                binding.locationTxt.error = "Location field is empty"
                Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show()
            }

            if (phoneNo.isEmpty()) {
                binding.phoneNoTxt.error = "Phone no field is empty"
                Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show()
            }

            if (name.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty()) {
                val newData: Map<String, String> = mapOf(
                    "name" to name,
                    "location" to location,
                    "phone" to phoneNo
                )



                updateData(newData)


            }
        }


    }


    private fun updateData(newData: Map<String, String>) {
        db.collection("users").document("Customer").collection("profile_data")
            .document(uid)
            .update(newData)
            .addOnSuccessListener {
                Toast.makeText(this, "Data is updated", Toast.LENGTH_LONG)
                    .show()


                //supportFragmentManager.beginTransaction().replace(R.id.profileFragment, profileFragment).commit()
                val intent = Intent(this, CustomerBottomNavActivity::class.java)
                intent.putExtra("fromProfile", true)
                startActivity(intent)


            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed to update data",
                    Toast.LENGTH_LONG
                ).show()
            }


    }
}