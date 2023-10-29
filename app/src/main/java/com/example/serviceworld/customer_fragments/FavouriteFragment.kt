package com.example.serviceworld.customer_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceworld.CustomerBottomNavActivity

import com.example.serviceworld.ServiceProviderRVAdapter
import com.example.serviceworld.databinding.FragmentFavouriteBinding

import com.example.serviceworld.model.ServiceProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class FavouriteFragment(customerBottomNavActivity: CustomerBottomNavActivity) : Fragment() {

    val context = customerBottomNavActivity
    lateinit var binding: FragmentFavouriteBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()


        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        binding.serviceProviderList.layoutManager = LinearLayoutManager(context)

        val list = arrayListOf<ServiceProviders>()

        val serviceProviderRVAdapter = ServiceProviderRVAdapter(context, list)

        binding.serviceProviderList.adapter = serviceProviderRVAdapter

        db.collection("users").document("Customer").collection("profile_data")
            .document(uid).collection("favourites")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.d("SERVICEPROVIDERS", error.message.toString())
                        return
                    }

                    for(documentChange : DocumentChange in value?.documentChanges!!){
                        if(documentChange.type == DocumentChange.Type.ADDED){
                            list.add(documentChange.document.toObject(ServiceProviders::class.java))
                        }
                    }

                    serviceProviderRVAdapter.notifyDataSetChanged()


                }

            })


        return binding.root
    }

}