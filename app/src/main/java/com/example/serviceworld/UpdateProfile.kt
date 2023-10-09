package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfile : AppCompatActivity() {

    lateinit var binding: ActivityUpdateProfileBinding
    lateinit var db: FirebaseFirestore

    lateinit var accountType: String

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var uid: String

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

        binding.roleddl.visibility = View.INVISIBLE
        binding.roleLable.visibility = View.INVISIBLE

        val services = resources.getStringArray(R.array.services)
        val serviceAdapter = ArrayAdapter(this, R.layout.dropdown_item, services)

        binding.roleName.setAdapter(serviceAdapter)

        accountType = intent.getStringExtra("accountType").toString()

        if (accountType == "Service Provider") {
            binding.roleddl.visibility = View.VISIBLE
            binding.roleName.visibility = View.VISIBLE
            binding.roleLable.visibility = View.VISIBLE
            binding.roleddl.hint = intent.getStringExtra("role")
        }

        binding.nameTxt.hint = intent.getStringExtra("name")
        binding.locationTxt.hint = intent.getStringExtra("location")
        binding.phoneNoTxt.hint = intent.getStringExtra("phoneNo")

        Log.d("DOCUMENTPATH3", accountType)

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

            if (accountType == "Service Provider") {
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

                    Log.d("DOCUMENTPATH4", accountType)

                    updateData(newData)

                }

            } else {

                if (name.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty()) {
                    val newData: Map<String, String> = mapOf(
                        "name" to name,
                        "location" to location,
                        "phone" to phoneNo
                    )

                    Log.d("DOCUMENTPATH5", accountType)

                    updateData(newData)


                }
            }


        }
    }

    fun updateData(newData: Map<String, String>) {
        db.collection("users").document(accountType).collection("profile_data")
            .document(uid)
            .update(newData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Data is updated", Toast.LENGTH_LONG)
                    .show()


                //supportFragmentManager.beginTransaction().replace(R.id.profileFragment, profileFragment).commit()
                val intent = Intent(applicationContext, BottomNavActivity::class.java)
                intent.putExtra("fromProfile", true)
                intent.putExtra("accountType", accountType)
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