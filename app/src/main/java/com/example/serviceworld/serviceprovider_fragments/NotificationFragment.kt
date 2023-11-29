package com.example.serviceworld.serviceprovider_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceworld.NotificationAdapter
import com.example.serviceworld.ServiceProviderBottomNavActivity
import com.example.serviceworld.databinding.FragmentNotificationBinding
import com.example.serviceworld.model.Notifications
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class NotificationFragment(serviceProviderBottomNavActivity: ServiceProviderBottomNavActivity) :
    Fragment() {

    val context = serviceProviderBottomNavActivity
    lateinit var binding: FragmentNotificationBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }


        val documentReference = db.collection("users").document("Service Provider").collection("profile_data").document(uid)

        documentReference.get().addOnSuccessListener { document ->
                if (document != null) {
                    val token = document.getString("fcmToken")

                    if (token != null) {
                        Log.d("NOTIFICATIONTOKEN", token)
                    }
                }
            }

        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        binding.notificationList.layoutManager = LinearLayoutManager(context)

        val list = arrayListOf<Notifications>()

        val notificationAdapter = NotificationAdapter(context, list)

        binding.notificationList.adapter = notificationAdapter

        documentReference.collection("requests").addSnapshotListener(object: EventListener<QuerySnapshot>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.d("SERVICEPROVIDERS", error.message.toString())
                    return
                }

                for(documentChange : DocumentChange in value?.documentChanges!!){
                    if(documentChange.type == DocumentChange.Type.ADDED){
                        list.add(documentChange.document.toObject(Notifications::class.java))
                    }
                }

                notificationAdapter.notifyDataSetChanged()
            }

        })

        return binding.root
    }

}