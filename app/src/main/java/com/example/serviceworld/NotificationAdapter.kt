package com.example.serviceworld

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.serviceworld.model.Notifications
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class NotificationAdapter(var context: Context, var list: ArrayList<Notifications>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var uid = ""
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(context) //use context instead
        val itemview = layoutInflater.inflate(R.layout.notification_listitem, parent, false)
        return ViewHolder(itemview)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var providerEmail = ""


        val notifications = list[position]
        holder.customerName.text = notifications.customerName
        holder.location.text = notifications.location
        holder.requestStatusTxt.visibility = View.INVISIBLE
        if (notifications.requestStatus == "Accepted" || notifications.requestStatus == "Rejected") {

            if(notifications.requestStatus == "Accepted"){
                holder.requestStatusTxt.visibility = View.VISIBLE
                holder.requestStatusTxt.text = notifications.requestStatus
                holder.requestStatusTxt.setTextColor(Color.parseColor("#4CAF50"))
            }else{
                holder.requestStatusTxt.visibility = View.VISIBLE
                holder.requestStatusTxt.text = notifications.requestStatus
                holder.requestStatusTxt.setTextColor(Color.parseColor("#F44336"))
            }
            holder.acceptText.visibility = View.INVISIBLE
            holder.rejectText.visibility = View.INVISIBLE
        }

        val firebaseUser = auth.currentUser

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }

        db.collection("users").document("Service Provider").collection("profile_data")
            .document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    providerEmail = document.getString("email").toString()
                }
            }

        holder.acceptText.setOnClickListener {
            //deleteDocument(uid, notifications)
            holder.acceptText.visibility = View.INVISIBLE
            holder.rejectText.visibility = View.INVISIBLE
            holder.requestStatusTxt.visibility = View.VISIBLE
            //accepted text

            holder.requestStatusTxt.text = context.getString(R.string.accepted_status)
            holder.requestStatusTxt.setTextColor(Color.parseColor("#4CAF50"))
            updateStatusRequest("Accepted", notifications, providerEmail)
        }


        holder.rejectText.setOnClickListener {
            holder.acceptText.visibility = View.INVISIBLE
            holder.rejectText.visibility = View.INVISIBLE
            holder.requestStatusTxt.visibility = View.VISIBLE
            //accepted text

            holder.requestStatusTxt.text = "Rejected"
            holder.requestStatusTxt.setTextColor(Color.parseColor("#F44336"))
            holder.deleteBtn.visibility = View.VISIBLE


            updateStatusRequest("Rejected", notifications, providerEmail)


        }

        holder.deleteBtn.setOnClickListener {
            val collectionReference =
                db.collection("users").document("Service Provider").collection("profile_data")
                    .document(uid).collection("requests")

            collectionReference.whereEqualTo("email", notifications.email).get()
                .addOnSuccessListener { documents ->
                    var id = ""

                    for (document in documents) {
                        id = document.id
                    }

                    collectionReference.document(id).delete().addOnSuccessListener {
                        Toast.makeText(context, "request is deleted", Toast.LENGTH_LONG).show()

                        context.startActivity(
                            Intent(
                                context,
                                ServiceProviderBottomNavActivity::class.java
                            )
                        )
                    }
                }
        }
    }

    private fun updateStatusRequest(
        requestStatus: String,
        notifications: Notifications,
        providerEmail: String
    ) {

        val updateData = mutableMapOf<String, Any>()
        updateData["requestStatus"] = requestStatus
        val customerCollectionReference =
            db.collection("users").document("Customer").collection("profile_data")
        customerCollectionReference.whereEqualTo("email", notifications.email).get()
            .addOnSuccessListener { documents ->
                var customer_id = ""

                for (document in documents) {
                    customer_id = document.id
                }

                var documentID = ""
                customerCollectionReference.document(customer_id).collection("requestedProviders")
                    .whereEqualTo("email", providerEmail).get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            documentID = document.id
                        }

                        customerCollectionReference.document(customer_id)
                            .collection("requestedProviders").document(documentID)
                            .update("requestStatus", requestStatus)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Customer's request updated",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Customer's request not updated",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                    }

            }

        val providerCollection = db.collection("users").document("Service Provider")
            .collection("profile_data")
            .document(uid).collection("requests")

        var requestId = ""
        providerCollection.whereEqualTo("email", notifications.email).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    requestId = document.id
                }

                providerCollection.document(requestId).set(updateData, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Request status updated in provider",
                            Toast.LENGTH_LONG
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Request status updated in provider",
                            Toast.LENGTH_LONG
                        ).show()
                    }

            }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val customerName = itemview.findViewById<TextView>(R.id.customerName)
        val location = itemview.findViewById<TextView>(R.id.location)
        val acceptText = itemview.findViewById<TextView>(R.id.acceptText)
        val rejectText = itemview.findViewById<TextView>(R.id.rejectText)
        val deleteBtn = itemview.findViewById<ImageView>(R.id.deleteBtn)
        val requestStatusTxt = itemview.findViewById<TextView>(R.id.requestStatusTxt)
    }



}
