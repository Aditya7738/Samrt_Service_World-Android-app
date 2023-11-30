package com.example.serviceworld

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceworld.databinding.ActivityServiceProviderDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ServiceProviderDetails : AppCompatActivity() {


    lateinit var binding: ActivityServiceProviderDetailsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var customerDocumentReference: DocumentReference
    var uid = ""
    // var token = ""

    var customerName = ""
    var location = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceProviderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        var providerName = ""
        var location = ""
        var email = ""
        var phoneNo = ""
        var serviceName = ""
        var availability = ""
        //var token = ""

        val collectionReference =
            db.collection("users").document("Service Provider").collection("profile_data")
        collectionReference
            .whereEqualTo("email", intent.getStringExtra("email"))
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (document in querySnapshot) {
                    val data = document.data
                    providerName = data["name"].toString()
                    location = data["location"].toString()
                    email = data["email"].toString()
                    phoneNo = data["phone"].toString()
                    serviceName = data["serviceName"].toString()
                    availability = data["isAvailable"].toString()
                  //  token = data["fcmToken"].toString()

                   // Log.d("insideTOKEN", token)
                }

               // Log.d("outsideTOKEN", token)
                binding.serviceProviderName.text = providerName
                binding.location.text = location
                binding.emailId.text = email
                binding.phoneNo.text = phoneNo
                binding.providedServiceName.text = serviceName
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }


        binding.callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNo")
            startActivity(intent)
        }

        binding.requestBtn.setOnClickListener {
            val reqProviderDetails = mutableMapOf<String, String?>()
            reqProviderDetails["name"] = providerName
            reqProviderDetails["serviceName"] = serviceName
            reqProviderDetails["email"] = email
            reqProviderDetails["requestStatus"] = "Requested"

            var prvoiderID = ""

            customerDocumentReference =
                db.collection("users").document("Customer").collection("profile_data").document(uid)

            var documentID = ""
            customerDocumentReference.collection("requestedProviders")
                .whereEqualTo("email", email).get()
                .addOnSuccessListener {documents ->
                    for (document in documents) {
                        documentID = document.id
                    }

                    customerDocumentReference.collection("requestedProviders").document(documentID).get().addOnSuccessListener{document ->
                        if(document != null) {
                            if(document.getString("requestStatus").toString() == "Rejected"){
                                addDataAndSendRequest(customerDocumentReference, reqProviderDetails)
                            }else{
                                Toast.makeText(this, "You already have send request to this service man", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }

                }
                .addOnFailureListener {
                    addDataAndSendRequest(customerDocumentReference, reqProviderDetails)
                }




            customerDocumentReference.get().addOnSuccessListener { document ->
                customerName = document.getString("name").toString()
                location = document.getString("location").toString()
                email = document.getString("email").toString()
            }



            collectionReference.whereEqualTo("email", intent.getStringExtra("email")).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        prvoiderID = document.id
                    }

                    val requestData = mutableMapOf<String, String>()
                    requestData["customerName"] = customerName
                    requestData["location"] = location
                    requestData["email"] = email

                    collectionReference.document(prvoiderID).collection("requests").add(requestData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Request sent", Toast.LENGTH_LONG).show()

                        }

                }

            //add dialog vbox to take service time [optional]
        }


    binding.favouriteBtn.setOnClickListener{
        val favProviderDetails = mutableMapOf<String, String?>()
        favProviderDetails["name"] = providerName
        favProviderDetails["serviceName"] = serviceName
        favProviderDetails["location"] = location
        favProviderDetails["isAvailable"] = availability


        db.collection("users").document("Customer").collection("profile_data").document(uid).collection("favourites")
            .add(favProviderDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Provider added in favourite list", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Not able to add in favourite list", Toast.LENGTH_LONG).show()
            }


    }




}

    private fun addDataAndSendRequest(customerDocumentReference: DocumentReference, reqProviderDetails: MutableMap<String, String?>) {
        customerDocumentReference.collection("requestedProviders")
            .add(reqProviderDetails)
            .addOnSuccessListener {
                //sendNotification(token)
                Toast.makeText(this, "Data added in collection", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Not able to send request", Toast.LENGTH_LONG).show()
            }
    }



//private fun sendNotification(token: String) {
//
//    //current username, msg, currentuserid, otherusertoken
//
//    customerDocumentReference.get().addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//
//            val result = task.result
//            val name = result.getString("name")
//            val location = result.getString("location")
//            //val token = result.getString("fcmToken")
//
//            try {
//                val jsonObject = JSONObject()
//                val notificationObject = JSONObject()
//                notificationObject.put("title", "Service Request")
//                notificationObject.put("body", name + "\n" + location)
//
//                val dataObject = JSONObject()
//                dataObject.put("userId", uid)
//
//                jsonObject.put("notificationObject", notificationObject)
//                jsonObject.put("dataObject", dataObject)
//                jsonObject.put("to", token)
//
//                callAPI(jsonObject)
//            } catch (e: Exception) {
//                Toast.makeText(this, "Don't abel to send notification", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//}
//
//private fun callAPI(jsonObject: JSONObject) {
//    val JSON: MediaType = "application/json; charset=utf-8".toMediaType() //code is different here
//
//    val client = OkHttpClient()
//
//    val url = "https://fcm.googleapis.com/fcm/send"
//
//    val body = jsonObject.toString().toRequestBody(JSON)
//    val request = Request.Builder()
//        .url(url)
//        .post(body)
//        .header(
//            "Authorization",
//            "Bearer AAAAyi1Rk20:APA91bHMPxTtNBIuG_Da0jFdZXpNzbSD1PpwzkWbPdC7njikW8gTmhC14m3D27jmv_0Rlcj3lNC34QZUWaxoeCWTGD1joWWZ1z2oyTK5_ubYExnQo1HALVrl8OS4c_LsEwYRWMUltOwy"
//        )
//        .build()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onFailure(call: Call, e: IOException) {
//            Log.d("CALLERROR", e.toString())
//        }
//
//        override fun onResponse(call: Call, response: Response) {
//            Log.d("RESPONSE", response.toString())
//        }
//    })
//
//}

}