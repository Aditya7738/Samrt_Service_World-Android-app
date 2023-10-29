package com.example.serviceworld.customer_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceworld.CustomerBottomNavActivity

import com.example.serviceworld.RequestedServiceAdapter
import com.example.serviceworld.databinding.FragmentRequestedservicesBinding
import com.example.serviceworld.model.RequestedServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class RequestedServicesFragment(customerBottomNavActivity: CustomerBottomNavActivity) : Fragment() {

    val context = customerBottomNavActivity
    lateinit var binding: FragmentRequestedservicesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRequestedservicesBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        var uid = ""
        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        binding.requestedProviderList.layoutManager = LinearLayoutManager(context)

        val list = arrayListOf<RequestedServices>()

        val requestedServiceAdapter = RequestedServiceAdapter(context, list)


       binding.requestedProviderList.adapter = requestedServiceAdapter

        db.collection("users").document("Customer").collection("profile_data")
            .document(uid).collection("requestedProviders")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.d("REQUESTED_PROVIDERS", error.message.toString())
                        return
                    }

                    for(documentChange : DocumentChange in value?.documentChanges!!){
                        if(documentChange.type == DocumentChange.Type.ADDED){
                            list.add(documentChange.document.toObject(RequestedServices::class.java))
                        }
                    }

                    requestedServiceAdapter.notifyDataSetChanged()


                }

            })



        return binding.root
    }

}