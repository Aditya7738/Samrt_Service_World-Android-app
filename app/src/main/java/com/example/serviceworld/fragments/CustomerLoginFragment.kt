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

class CustomerLoginFragment(loginActivity: LoginActivity) : Fragment() {

    var context = loginActivity
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customer_login, container, false)

        val emailTxt = view.findViewById<TextInputEditText>(R.id.lemailTxt)
        val passTxt = view.findViewById<TextInputEditText>(R.id.lpassTxt)

        val login: Button = view.findViewById(R.id.loginBtn)
        val registerTxt: TextView = view.findViewById(R.id.registerTxt)

        firebaseAuth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val pass = passTxt.text.toString()
            val email = emailTxt.text.toString()

            if(email.isEmpty()){
                Toast.makeText(context, "Email is empty", Toast.LENGTH_LONG).show()
            }

            if(pass.isEmpty()){
                Toast.makeText(context, "Password is empty", Toast.LENGTH_LONG).show()
            }

            if (email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(context, "Login successfully", Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        registerTxt.setOnClickListener{
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}