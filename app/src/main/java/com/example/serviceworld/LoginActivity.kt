package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityLoginBinding
import com.example.serviceworld.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fireAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireAuth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener(){
            val email = binding.lemailTxt.toString()
            val pass = binding.lpassTxt.toString()

            if(email.isEmpty()){
                Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show()
            }

            if(pass.isEmpty()){
                Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show()
            }

            if (email.isNotEmpty() && pass.isNotEmpty()){
                fireAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(){
                    if(it.isSuccessful){
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        binding.registerTxt.setOnClickListener(){
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}