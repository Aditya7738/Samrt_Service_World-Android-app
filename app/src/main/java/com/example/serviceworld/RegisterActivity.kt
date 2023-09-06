package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var nameEt: TextInputEditText
    lateinit var emailEt: TextInputEditText
    lateinit var phoneEt: TextInputEditText
    lateinit var locationEt: TextInputEditText
    lateinit var passEt: TextInputEditText
    lateinit var cpassEt: TextInputEditText
    lateinit var signupBtn: Button
    lateinit var loginTxt: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEt = findViewById(R.id.nameTxt)
        emailEt = findViewById(R.id.emailTxt)
        phoneEt = findViewById(R.id.phonenoTxt)
        locationEt = findViewById(R.id.locationTxt)
        passEt = findViewById(R.id.passTxt)
        cpassEt = findViewById(R.id.confirmpassTxt)
        loginTxt = findViewById(R.id.loginTxt)

        signupBtn = findViewById(R.id.signupBtn)

        firebaseAuth = FirebaseAuth.getInstance()

        loginTxt.setOnClickListener(){
            navigateToLogin()
        }


    }

    fun signUp(view: View) {
        val email = emailEt.text.toString()
        val pass = passEt.text.toString()
        val cpass = cpassEt.text.toString()
        val phone = phoneEt.text.toString()
        val location = locationEt.text.toString()

        if(email.isEmpty()){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_LONG).show();
        }

        if(pass.isEmpty()){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_LONG).show();
        }

        if(cpass.isEmpty()){
            Toast.makeText(this, "Confirm password field is empty", Toast.LENGTH_LONG).show();
        }

        if(phone.isEmpty()){
            Toast.makeText(this, "Phone field is empty", Toast.LENGTH_LONG).show();
        }

        if(location.isEmpty()){
            Toast.makeText(this, "Location field is empty", Toast.LENGTH_LONG).show();
        }



        if(email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty()){
            if(pass != cpass){
                Toast.makeText(this, "Password and confirm password are not same", Toast.LENGTH_LONG).show();
            }

            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(){
                if(it.isSuccessful){
                    navigateToLogin()
                }
            }
        }



    }

    fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}