package com.example.serviceworld

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serviceworld.model.RequestedServices

class RequestedServiceAdapter(var context: Context, var list: ArrayList<RequestedServices>):
    RecyclerView.Adapter<RequestedServiceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(context) //use context instead
        val itemView = layoutInflater.inflate(R.layout.requested_service_listitem, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val requestedServices = list[position]

        holder.serviceProvider.text = requestedServices.name
        holder.serviceName.text = requestedServices.serviceName
        holder.requestStatus.text = requestedServices.requestStatus

        when(requestedServices.requestStatus){
            "Accepted" -> holder.requestStatus.setTextColor(Color.parseColor("#4CAF50"))
            "Rejected" -> holder.requestStatus.setTextColor(Color.parseColor("#F44336"))
            else -> holder.requestStatus.setTextColor(Color.parseColor("#FF9800"))
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val serviceProvider = itemView.findViewById<TextView>(R.id.requested_providerName)
        val serviceName = itemView.findViewById<TextView>(R.id.requested_service_name)
        val requestStatus = itemView.findViewById<TextView>(R.id.requestStatus)
    }
}