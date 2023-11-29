package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    var selectedAccount: String = ""

    var selectedService: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.servicesddl.visibility = View.INVISIBLE

        val accountTypes = resources.getStringArray(R.array.accounts)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, accountTypes)

        val services = resources.getStringArray(R.array.services)
        val serviceAdapter = ArrayAdapter(this, R.layout.dropdown_item, services)

        binding.services.setAdapter(serviceAdapter)

        binding.accountType.setAdapter(arrayAdapter)

        binding.accountType.setOnItemClickListener { adapterView, view, position, id ->
            selectedAccount = adapterView.getItemAtPosition(position).toString()
            if (selectedAccount == "Service Provider") {
                binding.servicesddl.visibility = View.VISIBLE
                binding.services.visibility = View.VISIBLE
            }else{
                binding.servicesddl.visibility = View.INVISIBLE
                binding.services.visibility = View.INVISIBLE
            }
        }

        binding.services.setOnItemClickListener { adapterView, view, position, id ->
            selectedService = adapterView.getItemAtPosition(position).toString()
        }


        Log.d("REGISTER", selectedAccount)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        binding.signupBtn.setOnClickListener {
            Log.d("AFTERCLICK", selectedAccount)
            val name = binding.nameTxt.text.toString()
            val email = binding.emailTxt.text.toString()
            val pass = binding.passTxt.text.toString()
            val cpass = binding.cpassTxt.text.toString()
            val phone = binding.phonenoTxt.text.toString()
            val location = binding.locationTxt.text.toString()

            if(!intent.getBooleanExtra("fromLogin", true)){
                if(phone != intent.getStringExtra("AuthPhoneNo")){
                    binding.phonenoTxt.error = "This is not your authorized phone number"
                    Toast.makeText(this, "Please enter the phone number that you authorized previously", Toast.LENGTH_LONG).show()
                }
            }




            if (selectedAccount.isEmpty()) {
                binding.accountType.error = "Account type is not selected"
                Toast.makeText(this, "You have not selected account type", Toast.LENGTH_LONG).show()
            }

            if (selectedAccount == "Service Provider") {
                if (selectedService.isEmpty()) {
                    binding.services.error = "Service is not selected"
                    Toast.makeText(this, "You don't have selected services", Toast.LENGTH_LONG)
                        .show()
                }
            }

            if (name.isEmpty()) {
                binding.nameTxt.error = "Name field is empty"
                Toast.makeText(this, "Name field is empty", Toast.LENGTH_LONG).show()
            }

            if (email.isEmpty()) {
                binding.emailTxt.error = "Email field is empty"
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_LONG).show()
            }

//            if (pass.length < 6) {
//                binding.passTxt.error = "Password is less than 6 characters"
//                Toast.makeText(this, "Password is less than 6 characters", Toast.LENGTH_LONG).show()
//            }

            if (pass.isEmpty()) {
                binding.passTxt.error = "Password field is empty"
                Toast.makeText(this, "Password field is empty", Toast.LENGTH_LONG).show()
            } else if (pass.length < 6) {
                binding.passTxt.error = "Password is less than 6 characters"
                Toast.makeText(this, "Password is less than 6 characters", Toast.LENGTH_LONG).show()
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

            if (pass != cpass) {
                Toast.makeText(this, "Password and confirm password are not same", Toast.LENGTH_LONG).show()
            }

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && pass.length >= 6 &&
                cpass.isNotEmpty() && phone.isNotEmpty() && location.isNotEmpty() && phone.length == 10 && pass == cpass
            ) {

                var userData: HashMap<String, String> = hashMapOf()

               // var accountAlreadyExist = true

               // val collectionReference = db.collection("users")

                if (selectedAccount == "Service Provider") {


                    FirebaseMessaging.getInstance().token.addOnCompleteListener{ task->
                        if(!task.isSuccessful){
                            return@addOnCompleteListener
                        }
                        val token = task.result
                        Log.d("TOKEN", token)

                        userData = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "phone" to phone,
                            "location" to location,
                            "serviceName" to selectedService,
                            "isAvailable" to "Yes",
                            "fcmToken" to token

                        )

//                        collectionReference.document("Service Provider").collection("profile_data")
//                            .whereEqualTo("email", email).get()
//                            .addOnSuccessListener{
//                                binding.emailTxt.error = "An account already exist with this email address As a service provider"
//                                Toast.makeText(this, "This email have used already", Toast.LENGTH_LONG).show()
//                            }
//                            .addOnFailureListener{
//                                accountAlreadyExist = false
//                            }


                    }


                } else {

                    userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "location" to location
                    )

//                    collectionReference.document("Customer").collection("profile_data")
//                        .whereEqualTo("email", email).get()
//                        .addOnSuccessListener{documents ->
//                            for(document in documents){
//                                Log.d("EMAIL", document.getString("email").toString())
//                            }
//                            binding.emailTxt.error = "An account already exist with this email address  As a customer"
//                            Toast.makeText(this, "This email have used already", Toast.LENGTH_LONG).show()
//                        }
//                        .addOnFailureListener{
//                            accountAlreadyExist = false
//                        }
                }

               // if(!accountAlreadyExist) {
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


                                    val uid = firebaseUser.uid

                                    if (selectedAccount == "Customer") {


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
                                    } else {

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
                            Toast.makeText(
                                this,
                                "",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            binding.emailTxt.error = "An account already exist with this email address"
                            Toast.makeText(this, "Registration is not successful", Toast.LENGTH_LONG).show()
                        }
                    }
              //  }


            }
        }

        binding.loginTxt.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}
