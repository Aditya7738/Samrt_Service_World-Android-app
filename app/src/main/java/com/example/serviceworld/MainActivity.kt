package com.example.serviceworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var name: TextView = findViewById<TextView>(R.id.appname)

        val animation: Animation = Animation(this)

        name.startAnimation(animation)

    }
}