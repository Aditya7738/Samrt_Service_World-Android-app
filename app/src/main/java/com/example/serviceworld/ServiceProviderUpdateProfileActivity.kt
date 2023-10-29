package com.example.serviceworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceworld.databinding.ActivityServiceProviderUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ServiceProviderUpdateProfileActivity : AppCompatActivity() {

    lateinit var uid: String
    lateinit var binding: ActivityServiceProviderUpdateProfileBinding

    lateinit var firebaseAuth: FirebaseAuth

    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceProviderUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        val services = resources.getStringArray(R.array.services)
        val serviceAdapter = ArrayAdapter(this, R.layout.dropdown_item, services)

        binding.roleName.setAdapter(serviceAdapter)
        binding.roleddl.hint = intent.getStringExtra("role")

        binding.nameTxt.hint = intent.getStringExtra("name")
        binding.locationTxt.hint = intent.getStringExtra("location")
        binding.phoneNoTxt.hint = intent.getStringExtra("phoneNo")


        binding.saveBtn.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            val location = binding.locationTxt.text.toString()
            val phoneNo = binding.phoneNoTxt.text.toString()
            val role = binding.roleName.text.toString()


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

            if (role.isEmpty()) {
                binding.roleName.error = "Service is not selected"
                Toast.makeText(this, "You don't have selected services", Toast.LENGTH_LONG)
                    .show()
            }

            if (name.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty() && role.isNotEmpty()) {
                val newData: Map<String, String> = mapOf(
                    "name" to name,
                    "location" to location,
                    "phone" to phoneNo,
                    "serviceName" to role
                )

                db.collection("users").document("Service Provider").collection("profile_data")
                    .document(uid)
                    .update(newData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data is updated", Toast.LENGTH_LONG)
                            .show()


                        //supportFragmentManager.beginTransaction().replace(R.id.profileFragment, profileFragment).commit()
                        val intent =
                            Intent(this, ServiceProviderBottomNavActivity::class.java)
                        intent.putExtra("fromProfile", true)
                        startActivity(intent)


                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            "Failed to update data",
                            Toast.LENGTH_LONG
                        ).show()
                    }

            }


        }
    }
}