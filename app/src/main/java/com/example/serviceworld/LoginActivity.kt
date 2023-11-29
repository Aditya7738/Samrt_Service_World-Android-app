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
import com.google.firebase.messaging.FirebaseMessaging

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

        binding.laccountType.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
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

                    Log.d("LOGINEMAIL", email)
                    collectionReference
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener { emailCheck ->
                            if (emailCheck.isSuccessful && !emailCheck.result.isEmpty) {
                                Log.d("CUSTOMER_success", "STEP1")
                                correctAccountSelected = true
                                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, CustomerBottomNavActivity::class.java)

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
                            if(emailCheck.isSuccessful && !emailCheck.result.isEmpty){
                                correctAccountSelected = true
                                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("SELECTED", "STEP2")
                                        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, ServiceProviderBottomNavActivity::class.java)

                                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                            if(task.isSuccessful) {
                                                val token = task.result
                                                Log.d("TOKEN", token)

                                                val firebaseUser = firebaseAuth.currentUser
                                                var uid = ""
                                                if (firebaseUser != null){
                                                    uid = firebaseUser.uid
                                                }

                                                collectionReference.whereEqualTo("fcmToken", token)
                                                    .get().addOnSuccessListener{documents ->
                                                        //if document is empty means user is trying to login into new device
                                                        if(documents.isEmpty){
                                                            collectionReference.document(uid).update("fcmToken", token)
                                                                .addOnSuccessListener{
                                                                    Toast.makeText(this, "Token for new device is updated successfully", Toast.LENGTH_LONG).show()
                                                            }
                                                                .addOnFailureListener{
                                                                    Toast.makeText(this, "Don't able to update new token", Toast.LENGTH_LONG).show()
                                                                }
                                                        }else{
                                                            collectionReference.document(uid).update("fcmToken", token)
                                                                .addOnSuccessListener{
                                                                    Toast.makeText(this, "New token is updated successfully", Toast.LENGTH_LONG).show()
                                                                }
                                                                .addOnFailureListener{
                                                                    Toast.makeText(this, "Don't able to update new token", Toast.LENGTH_LONG).show()
                                                                }
                                                        }
                                                }
                                            }else{
                                                Toast.makeText(this, "Don't able to generate token", Toast.LENGTH_LONG).show()
                                            }
                                        }

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
            }
        }

        binding.registerTxt.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("fromLogin", true)
            startActivity(intent)
        }

    }
//    private fun getFCMToken(){
//        var token= ""
//
//    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if(firebaseAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            //use return
            //return
        }
    }
}