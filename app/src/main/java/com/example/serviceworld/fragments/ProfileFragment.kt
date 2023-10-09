package com.example.serviceworld.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.serviceworld.UpdateProfile
import com.example.serviceworld.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment() : Fragment() {

    var documentPath: String = ""
    constructor(selectedAccount: String) : this(){
        documentPath = selectedAccount
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    //private var documentName: String = if (documentPath == "Customer") "Customer" else "Service Provider"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.roleName.visibility = View.INVISIBLE

        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        Log.d("DOCUMENTPATH", documentPath)


        Log.d("UID----------------", uid)

        if(documentPath == "Service Provider") {
            binding.roleName.visibility = View.VISIBLE

            db.collection("users").document(documentPath).collection("profile_data")
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
        }else{
            db.collection("users").document(documentPath).collection("profile_data")
                .document(uid).get().addOnSuccessListener { document ->
                    if (document != null) {

                        binding.emailId.text = document.getString("email")
                        binding.location.text = document.getString("location")
                        binding.phoneNo.text = document.getString("phone")
                        binding.name.text = document.getString("name")

                        Log.d("GETDATA", document.getString("name").toString())
                    }
                }
        }


        //binding.profileImage.setImageResource()

        binding.updateBtn.setOnClickListener {

            val location = binding.location.text.toString()
            val phoneNo = binding.phoneNo.text.toString()
            val name = binding.name.text.toString()
            var roleName = binding.roleName.text.toString()

            if(documentPath == "Service Provider"){
                roleName = binding.roleName.text.toString()
            }

            Log.d("DOCUMENTPATH2", documentPath)

            val intent = Intent(requireContext(), UpdateProfile::class.java)
            intent.putExtra("serviceName", roleName)
            intent.putExtra("location", location)
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("name", name)
            intent.putExtra("accountType", documentPath)
            Log.d("ROLENAME", roleName)


            startActivity(intent)
        }

        return binding.root
    }

}