package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    var selectedAccount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accountTypes = resources.getStringArray(R.array.accounts)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, accountTypes)

        binding.accountType.setAdapter(arrayAdapter)

        binding.accountType.setOnItemClickListener { adapterView, view, position, id ->
            selectedAccount = adapterView.getItemAtPosition(position).toString()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.signupBtn.setOnClickListener {
            val name = binding.nameTxt.text.toString()
            val email = binding.emailTxt.text.toString()
            val pass = binding.passTxt.text.toString()
            val cpass = binding.cpassTxt.text.toString()
            val phone = binding.phonenoTxt.text.toString()
            val location = binding.locationTxt.text.toString()

            if(selectedAccount.isEmpty()){
                binding.accountType.error = "Account type is not selected"
                Toast.makeText(this, "You have not selected account type", Toast.LENGTH_LONG).show()
            }

            if (name.isEmpty()) {
                binding.nameTxt.error = "Name field is empty"
                Toast.makeText(this, "Name field is empty", Toast.LENGTH_LONG).show()
            }

            if (email.isEmpty()) {
                binding.emailTxt.error = "Email field is empty"
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_LONG).show()
            }

            if (pass.length < 6) {
                binding.passTxt.error = "Password is less than 6 characters"
                Toast.makeText(this, "Password is less than 6 characters", Toast.LENGTH_LONG).show()
            }

            if (pass.isEmpty()) {
                binding.passTxt.error = "Password field is empty"
                Toast.makeText(this, "Password field is empty", Toast.LENGTH_LONG).show()
            }
            if (cpass.isEmpty()) {
                binding.cpassTxt.error = "Confirm password field is empty"
                Toast.makeText(this, "Confirm password field is empty", Toast.LENGTH_LONG).show()
            }

            if (phone.isEmpty()) {
                binding.phonenoTxt.error = "Phone number field is empty"
                Toast.makeText(this, "Phone field is empty", Toast.LENGTH_LONG).show()
            }

            if (phone.length != 10) {
                binding.phonenoTxt.error = "Phone number is not valid"
                Toast.makeText(this, "Phone number is not valid", Toast.LENGTH_LONG).show()
            }

            if (location.isEmpty()) {
                binding.locationTxt.error = "Location field is empty"
                Toast.makeText(this, "Location field is empty", Toast.LENGTH_LONG).show()
            }

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && pass.length >= 6 &&
                cpass.isNotEmpty() && phone.isNotEmpty() && location.isNotEmpty() && phone.length == 10
            ) {
                if (pass != cpass) {
                    Toast.makeText(
                        this,
                        "Password and confirm password are not same",
                        Toast.LENGTH_LONG
                    ).show()
                }


                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        firebaseUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Verification email have sent to you",
                                    Toast.LENGTH_LONG
                                ).show()
                                val userData: HashMap<String, String> = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "phone" to phone,
                                    "location" to location
                                )

                                val uid = firebaseUser.uid

                                if(selectedAccount == "Customer") {


                                    db.collection("users")
                                        .document("Customer")
                                        .collection("profile_data")
                                        .document(uid)
                                        .set(userData).addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Customer's data have stored successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }else{

                                    db.collection("users")
                                        .document("Service Provider")
                                        .collection("profile_data")
                                        .document(uid)
                                        .set(userData).addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Service provider's data have stored successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }

                                navigateToLogin()
                            }?.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Fail to send verification email",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        Toast.makeText(this, "Registration is not successful", Toast.LENGTH_LONG)
                            .show()
                    }
                }


            }
        }

        binding.loginTxt.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }



}
