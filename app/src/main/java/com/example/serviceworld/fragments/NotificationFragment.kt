package com.example.serviceworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.serviceworld.BottomNavActivity
import com.example.serviceworld.R
import com.example.serviceworld.databinding.FragmentNotificationBinding


class NotificationFragment(bottomNavActivity: BottomNavActivity) : Fragment() {

    lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        return binding.root
    }

}