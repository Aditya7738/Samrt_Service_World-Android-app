package com.example.serviceworld


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager

import android.widget.LinearLayout
import android.widget.Toast
import com.example.serviceworld.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val linearLayout = findViewById<LinearLayout>(R.id.linearL1)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firebaseUser = auth.currentUser


        val slideFromRight = Slide()
        slideFromRight.slideEdge = Gravity.END
        TransitionManager.beginDelayedTransition(linearLayout, slideFromRight)
        mainBinding.appname1.visibility = View.VISIBLE
        mainBinding.appname2.visibility = View.VISIBLE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )



        val collectionReference = db
            .collection("users").document("Customer").collection("profile_data")

        Handler(Looper.getMainLooper()).postDelayed({



            if (firebaseUser != null) {
                Log.d("FIREBASEUSER", firebaseUser.uid)

                collectionReference.get().addOnSuccessListener { documents ->

                    //var idArray:Array<String> = Array(documents.size()){""}

                    val idArrayList = mutableListOf<String>()

                    for (document in documents) {
                        idArrayList.add(document.id)
                    }

                    for(id in idArrayList){
                        Log.d("IDS", id)
                    }

                    if(firebaseUser.uid in idArrayList){
                        Log.d("FIREBASEUSER_UID", firebaseUser.uid)
                        startActivity(Intent(this, CustomerBottomNavActivity::class.java))
                    }else{
                        Log.d("FIREBASEUSER_UID", firebaseUser.uid)
                        startActivity(Intent(this, ServiceProviderBottomNavActivity::class.java))
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Not able to fetch ids", Toast.LENGTH_LONG).show()
                    }


            } else {
                Log.d("FIREBASEUSER_UID", firebaseUser?.uid.toString())

                startActivity(Intent(this, OtpCheckActivity::class.java))

            }
            //startActivity(intent)
            finish()
        }, 3000)
    }


}