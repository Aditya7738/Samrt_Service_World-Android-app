package com.example.serviceworld

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.serviceworld.databinding.ActivityOtpCheckBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpCheckActivity : AppCompatActivity() {


    lateinit var binding: ActivityOtpCheckBinding

    lateinit var firebaseAuth: FirebaseAuth

    private var firebaseUser: FirebaseUser? = null

    lateinit var smsCode: String

    lateinit var storeVerificationId: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runtimePermission()

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseUser = firebaseAuth.currentUser

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken){
                storeVerificationId = verificationId
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                smsCode = p0.smsCode.toString()

                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storeVerificationId, smsCode)

                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            binding.otpTxt.hint = smsCode
                            Toast.makeText(applicationContext,"OTP have sent successfully", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext,"Failed to send OTP", Toast.LENGTH_LONG).show()
                        }

                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Could not send OTP", Toast.LENGTH_LONG).show()

                if(e is FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }else if (e is FirebaseTooManyRequestsException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            }




        }

        binding.sendOTPBtn.setOnClickListener{
            val phoneNo = "+91 " + binding.phoneTxt.text

            if(phoneNo.isEmpty()){
                Toast.makeText(this, "Phone no field is empty", Toast.LENGTH_LONG).show()
            }

            if(binding.phoneTxt.text.toString().length != 10){
                Toast.makeText(this, "Phone no field is empty", Toast.LENGTH_LONG).show()
            }

            if(phoneNo.isNotEmpty() && binding.phoneTxt.text.toString().length == 10){
                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setPhoneNumber(phoneNo)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

        binding.submitBtn.setOnClickListener{
            val otp = binding.otpTxt.text.toString()

            if(otp.isEmpty()){
                Toast.makeText(applicationContext, "OTP field is empty", Toast.LENGTH_SHORT).show()
            }else{
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storeVerificationId, otp)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            startActivity(Intent(this, RegisterActivity::class.java)
                                .putExtra("AuthPhoneNo", binding.phoneTxt.text.toString()))

                        }else if(task.exception is FirebaseAuthInvalidCredentialsException)
                            Toast.makeText(applicationContext,task.exception.toString(), Toast.LENGTH_LONG).show()
                        }

                    }


            }

        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun runtimePermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            val permissionStringArray= arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.POST_NOTIFICATIONS)
            requestPermissions(permissionStringArray, 100)
        }
    }


    }