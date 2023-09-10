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

class UserFragment(registerActivity: RegisterActivity) : Fragment() {

    var context = registerActivity
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val nameTxt = view.findViewById<TextInputEditText>(R.id.nameTxt)
        val emailTxt = view.findViewById<TextInputEditText>(R.id.emailTxt)
        val phoneNoTxt = view.findViewById<TextInputEditText>(R.id.phonenoTxt)
        val locationTxt = view.findViewById<TextInputEditText>(R.id.locationTxt)
        val passTxt = view.findViewById<TextInputEditText>(R.id.passTxt)
        val cpassTxt = view.findViewById<TextInputEditText>(R.id.cpassTxt)
        val signUp: Button = view.findViewById(R.id.signupBtn)
        val loginTxt: TextView = view.findViewById(R.id.loginTxt)

        firebaseAuth = FirebaseAuth.getInstance()
        
        signUp.setOnClickListener{
            val name = nameTxt.text.toString()
            val email = emailTxt.text.toString()
            val pass = passTxt.text.toString()
            val cpass = cpassTxt.text.toString()
            val phone = phoneNoTxt.text.toString()
            val location = locationTxt.text.toString()

            if(name.isEmpty()) {
                nameTxt.error = "Name field is empty"
                Toast.makeText(context, "Name field is empty", Toast.LENGTH_LONG).show()
            }

            if(email.isEmpty()){
                emailTxt.error = "Email field is empty"
                Toast.makeText(context, "Email field is empty", Toast.LENGTH_LONG).show()
            }

            if(pass.length < 6){
                passTxt.error = "Password is less than 6 characters"
                Toast.makeText(context, "Password is less than 6 characters", Toast.LENGTH_LONG).show()
            }

            if(pass.isEmpty()){
                passTxt.error = "Password field is empty"
                Toast.makeText(context, "Password field is empty", Toast.LENGTH_LONG).show()
            }
            if(cpass.isEmpty()){
                cpassTxt.error = "Confirm password field is empty"
                Toast.makeText(context, "Confirm password field is empty", Toast.LENGTH_LONG).show()
            }

            if(phone.isEmpty()){
                phoneNoTxt.error = "Phone number field is empty"
                Toast.makeText(context, "Phone field is empty", Toast.LENGTH_LONG).show()
            }

            if(location.isEmpty()){
                locationTxt.error = "Location field is empty"
                Toast.makeText(context, "Location field is empty", Toast.LENGTH_LONG).show()
            }

            if(name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && pass.length >= 6 &&
                cpass.isNotEmpty() && phone.isNotEmpty() && location.isNotEmpty()){
                if(pass != cpass){
                    Toast.makeText(context, "Password and confirm password are not same", Toast.LENGTH_LONG).show()
                }

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        val firebaseUser = firebaseAuth.currentUser
                        firebaseUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(context, "Verification email have sent to you", Toast.LENGTH_LONG).show()
                            navigateToLogin()
                        }?.addOnFailureListener{
                            Toast.makeText(context, "Fail to send verification email", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(context, "Registration is not successful", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        loginTxt.setOnClickListener{
            navigateToLogin()
        }
        
        return view
    }


    private fun navigateToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

}