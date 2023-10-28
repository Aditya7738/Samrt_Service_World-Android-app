package com.example.serviceworld

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceworld.databinding.ActivityServiceProvidersBinding
import com.example.serviceworld.model.ServiceProviders
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ServiceProvidersActivity : AppCompatActivity() {

    lateinit var binding: ActivityServiceProvidersBinding
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceProvidersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        Log.d("SERVICEPROVIDER", "ACTIVITY")
        db = FirebaseFirestore.getInstance()

        binding.serviceProviderList.layoutManager = LinearLayoutManager(this)

        val list = arrayListOf<ServiceProviders>()

        val serviceProviderRVAdapter = ServiceProviderRVAdapter(this, list)


        binding.serviceProviderList.adapter = serviceProviderRVAdapter

        val docRef = db.collection("users").document("Service Provider").collection("profile_data")

        docRef.whereEqualTo("serviceName", intent.getStringExtra("selectedService"))
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
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
    }


}
