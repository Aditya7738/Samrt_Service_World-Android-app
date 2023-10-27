package com.example.serviceworld

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.example.serviceworld.databinding.ActivityServiceProviderDetailsBinding

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore


class ServiceProviderDetails : AppCompatActivity() {

    lateinit var binding: ActivityServiceProviderDetailsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceProviderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }


//        db.collection("users").document("Customer").collection("profile_data")
//            .document(uid).get().addOnSuccessListener{document->
//                userName = document.getString("name")
//            }

        var providerName = ""
        var location = ""
        var email = ""
        var phoneNo = ""
        var serviceName = ""
        var availability = ""

        val collectionReference =
            db.collection("users").document("Service Provider").collection("profile_data")
        collectionReference
            .whereEqualTo("email", intent.getStringExtra("email"))
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot) {
                    val data = document.data
                    providerName = data["name"].toString()
                    location = data["location"].toString()
                    email = data["email"].toString()
                    phoneNo = data["phone"].toString()
                    serviceName = data["serviceName"].toString()
                    availability = data["isAvailable"].toString()
                }
                binding.serviceProviderName.text = providerName
                binding.location.text = location
                binding.emailId.text = email
                binding.phoneNo.text = phoneNo
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
//            .get()
//            .addOnSuccessListener {
//                if (document != null) {
//                    binding.serviceProviderName.text = document.getString("name")
//                    binding.location.text = document.getString("location")
//                    binding.emailId.text = document.getString("email")
//                    binding.phoneNo.text = document.getString("phone")
//
//                }
//            }

        binding.callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + binding.phoneNo.text)
            startActivity(intent)
        }

        binding.requestBtn.setOnClickListener {

        }

        binding.favouriteBtn.setOnClickListener {
            val favProviderNames = mutableMapOf<String, String?>()
            favProviderNames["name"] = providerName
            favProviderNames["serviceName"] = serviceName
            favProviderNames["location"] = location
            favProviderNames["isAvailable"] = availability


//            db.collection("users").document("Service Provider").collection("profile_data")
//                .whereEqualTo("email", intent.getStringExtra("email"))
//                .get().addOnCompleteListener{
//
//                }

            db.collection("users").document("Customer").collection("profile_data")
                .document(uid).collection("favourites")
                .add(favProviderNames)
                .addOnSuccessListener {
                    Toast.makeText(this, "Provider added in favourite list", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Not able to add in favourite list", Toast.LENGTH_LONG).show()
                }


        }

        binding.reportBtn.setOnClickListener {

        }


    }

}