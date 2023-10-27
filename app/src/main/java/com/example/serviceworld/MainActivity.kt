package com.example.serviceworld


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager

import android.widget.LinearLayout
import com.example.serviceworld.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val linearLayout = findViewById<LinearLayout>(R.id.linearL1)



        val slideFromRight = Slide()
        slideFromRight.slideEdge = Gravity.END
        TransitionManager.beginDelayedTransition(linearLayout, slideFromRight)
        mainBinding.appname1.visibility = View.VISIBLE
        mainBinding.appname2.visibility = View.VISIBLE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, OtpCheckActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }


}