package com.example.serviceworld.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.serviceworld.LoginActivity
import com.example.serviceworld.R
import com.example.serviceworld.RegisterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SProviderFragment(registerActivity: RegisterActivity) : Fragment() {

    var context = registerActivity
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_s_provider, container, false)

        val nameTxt = view.findViewById<TextInputEditText>(R.id.snameTxt)
        val emailTxt = view.findViewById<TextInputEditText>(R.id.semailTxt)
        val phoneNoTxt = view.findViewById<TextInputEditText>(R.id.sphonenoTxt)
        val locationTxt = view.findViewById<TextInputEditText>(R.id.slocationTxt)
        val passTxt = view.findViewById<TextInputEditText>(R.id.spassTxt)
        val cpassTxt = view.findViewById<TextInputEditText>(R.id.scpassTxt)
        val signUp: Button = view.findViewById<Button>(R.id.ssignupBtn)
        val loginTxt: TextView = view.findViewById(R.id.sloginTxt)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        signUp.setOnClickListener(){
            val name = nameTxt.text.toString()
            val email = emailTxt.text.toString()
            val pass = passTxt.text.toString()
            val cpass = cpassTxt.text.toString()
            val phone = phoneNoTxt.text.toString()
            val location = locationTxt.text.toString()

            if(name.isEmpty()) {
                nameTxt.setError("Name field is empty")
                Toast.makeText(context, "Name field is empty", Toast.LENGTH_LONG).show()
            }

            if(email.isEmpty()){
                emailTxt.setError("Email field is empty")
                Toast.makeText(context, "Email field is empty", Toast.LENGTH_LONG).show()
            }

            if(pass.length < 6){
                passTxt.setError("Password is less than 6 characters")
                Toast.makeText(context, "Password is less than 6 characters", Toast.LENGTH_LONG).show()
            }

            if(pass.isEmpty()){
                passTxt.setError("Password field is empty")
                Toast.makeText(context, "Password field is empty", Toast.LENGTH_LONG).show()
            }

            if(cpass.isEmpty()){
                cpassTxt.setError("Confirm password field is empty")
                Toast.makeText(context, "Confirm password field is empty", Toast.LENGTH_LONG).show()
            }

            if(phone.isEmpty()){
                phoneNoTxt.setError("Phone number field is empty")
                Toast.makeText(context, "Phone field is empty", Toast.LENGTH_LONG).show()
            }

            if(location.isEmpty()){
                locationTxt.setError("Location field is empty")
                Toast.makeText(context, "Location field is empty", Toast.LENGTH_LONG).show()
            }

            if(name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && pass.length >= 6 &&
                cpass.isNotEmpty() && phone.isNotEmpty() && location.isNotEmpty()){
                if(pass != cpass){
                    Toast.makeText(context, "Password and confirm password are not same", Toast.LENGTH_LONG).show()
                }

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(){
                    if(it.isSuccessful){
                        val firebaseUser = firebaseAuth.currentUser
                        firebaseUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(context, "Verification email have sent to you", Toast.LENGTH_LONG).show()

                            val userData: HashMap<String, Any> = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "phone" to phone,
                                "location" to location
                            )

                            val uid = firebaseUser.uid


                            db.collection("users")
                                .document("service_provider").collection("profile_data").document(uid)
                                .set(userData).addOnSuccessListener{
                                    Toast.makeText(context, "Customer's data have stored successfully", Toast.LENGTH_LONG).show()
                                }

                            navigateToLogin()
                        }?.addOnFailureListener(){
                            Toast.makeText(context, "Fail to send verification email", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(context, "Registration is not successful", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        loginTxt.setOnClickListener(){
            navigateToLogin()
        }

        return view
    }

    fun navigateToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

}