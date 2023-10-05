package com.example.serviceworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var selectedAccount: String = ""
    var correctAccountSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val arr = resources.getStringArray(R.array.accounts)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, arr)

        binding.laccountType.setAdapter(arrayAdapter)

        binding.laccountType.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            selectedAccount = adapterView.getItemAtPosition(position).toString()
        }

        binding.loginBtn.setOnClickListener {
            val pass = binding.lpassTxt.text.toString()
            val email = binding.lemailTxt.text.toString()



            if (email.isEmpty()) {
                binding.lemailTxt.error = "Email field is empty"
                Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show()
            }

            if (pass.isEmpty()) {
                binding.lpassTxt.error = "Password field is empty"
                Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show()
            }

            if(selectedAccount.isEmpty()){
                binding.laccountType.error = "Account type is not selected"
                Toast.makeText(this, "You have not selected account type", Toast.LENGTH_LONG).show()
            }

            if (email.isNotEmpty() && pass.isNotEmpty() && selectedAccount.isNotEmpty()) {


                if(selectedAccount == "Customer"){
                    Log.d("CUSTOMER", "STEP1")
                    val collectionReference = db.collection("users")
                        .document("Customer")
                        .collection("profile_data")


                    collectionReference.whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener { emailCheck ->
                            if (emailCheck.isSuccessful) {
                                Log.d("CUSTOMER_success", "STEP1")
                                correctAccountSelected = true
                                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, BottomNavActivity::class.java)
                                        intent.putExtra("AccountType", selectedAccount)
                                        startActivity(intent)

                                    } else {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "password is wrong", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Log.d("CUSTOMER", "STEP2")
                                binding.laccountType.error = "You have chosen wrong account"
                                Toast.makeText(
                                    this,
                                    "You have chosen wrong account",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                       //if(it != null){
                           //usersEmail = it.data!!["email"].toString()
                       //usersEmail = it.getString("email").toString()
                       //}
                }else{
                    Log.d("SERVICE", "STEP1")
                    val collectionReference = db.collection("users")
                        .document("Service Provider")
                        .collection("profile_data")


                    collectionReference.whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener{ emailCheck ->
                            if(emailCheck.isSuccessful){
                                correctAccountSelected = true
                                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, BottomNavActivity::class.java)
                                        intent.putExtra("AccountType", selectedAccount)
                                        startActivity(intent)

                                    } else {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "password is wrong", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }else{
                                Log.d("SERVICE", "STEP2")
                                binding.laccountType.error = "You have chosen wrong account"
                                Toast.makeText(this, "You have chosen wrong account", Toast.LENGTH_LONG).show()
                            }
                        }
                }

//               if(correctAccountSelected) {
//                   Log.d("SELECTED", "STEP1")
//                   firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
//                       if (it.isSuccessful) {
//                           Log.d("SELECTED", "STEP2")
//                           Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
//                           val intent = Intent(this, BottomNavActivity::class.java)
//                           intent.putExtra("AccountType", selectedAccount)
//                           startActivity(intent)
//
//                       } else {
//                           Log.d("SELECTED", "STEP2")
//                           Toast.makeText(this, "Either email or password is wrong", Toast.LENGTH_LONG).show()
//                       }
//                   }
//               }
//                else{
//                    Toast.makeText(this, "You have chosen wrong account", Toast.LENGTH_LONG).show()
//                }




            }
        }

        binding.registerTxt.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}