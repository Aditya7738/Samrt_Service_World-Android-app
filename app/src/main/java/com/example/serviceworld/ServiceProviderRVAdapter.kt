package com.example.serviceworld

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serviceworld.model.ServiceProviders

class ServiceProviderRVAdapter(var context: Context, var list: ArrayList<ServiceProviders>):
    RecyclerView.Adapter<ServiceProviderRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutInflater = LayoutInflater.from(context) //use context instead
        val itemview = layoutInflater.inflate(R.layout.servicepoviders_listitem, parent, false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serviceProviders = list[position]

        holder.serviceProvider.text = serviceProviders.name
        holder.serviceName.text = serviceProviders.serviceName
        holder.location.text = serviceProviders.location
        holder.availableOrNotTxt.text = serviceProviders.isAvailable
    }

    override fun getItemCount(): Int {
        Log.d("NUMBER_ITEMS", list.size.toString())
        return list.size
    }

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val serviceProvider = itemview.findViewById<TextView>(R.id.serviceProviderName)
        val serviceName = itemview.findViewById<TextView>(R.id.service_name)
        val location = itemview.findViewById<TextView>(R.id.locTxt)
        val availableOrNotTxt = itemview.findViewById<TextView>(R.id.availableOrNotTxt)
    }

}