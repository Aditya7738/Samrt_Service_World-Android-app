package com.example.serviceworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.serviceworld.BottomNavActivity
import com.example.serviceworld.R
import com.example.serviceworld.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment() : Fragment() {

    ///watch foxandroid video send data via rest api
    //var context = bottomNavActivity
    lateinit var binding: FragmentProfileBinding
    lateinit var db: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    //val documentPath = accountType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        var uid: String = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        binding = FragmentProfileBinding.inflate(inflater, container, false)

//        db.collection("users").document(documentPath).collection("profile_data")
//            .document(uid).get().addOnSuccessListener { document ->
//                if (document != null) {
////                    binding.emailId.text = document.data!!["email"].toString()
////                    binding.location.text = document.data!!["location"].toString()
////                    binding.phoneNo.text = document.data!!["phone"].toString()
////                    binding.name.text = document.data!!["name"].toString()
//
//                    //data binding or use view
//
//                    binding.emailId.text = document.getString("email")
//                    binding.location.text = document.getString("location")
//                    binding.phoneNo.text = document.getString("phone")
//                    binding.name.text = document.getString("name")
//                }
//            }
        //binding.profileImage.setImageResource()

        binding.updateBtn.setOnClickListener {

            val email = binding.emailId.text.toString()
            val location = binding.location.text.toString()
            val phoneNo = binding.phoneNo.text.toString()
            val name = binding.name.text.toString()

            val result: Bundle = Bundle()
            result.putString("email", email)
            result.putString("location", location)
            result.putString("phoneNo", phoneNo)
            result.putString("name", name)
          //  result.putString("accountType", documentPath)

            parentFragmentManager.setFragmentResult("oldData", result)
//
//
//            val fragment = UpdateProfileFragment()
//            val fragmentTransaction = fragmentManager?.beginTransaction()
//            fragmentTransaction?.replace(R.id.profile_fragment, fragment)
//            fragmentTransaction?.commit()
        }

        navigateUpdateProfile()

        return binding.root
    }

    private fun navigateUpdateProfile() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment())
    }


}