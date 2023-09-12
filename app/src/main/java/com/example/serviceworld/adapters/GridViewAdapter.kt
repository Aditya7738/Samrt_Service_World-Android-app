package com.example.serviceworld.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.serviceworld.R
import com.example.serviceworld.model.ServiceModel

class GridViewAdapter(var context: Context, var serviceList: ArrayList<ServiceModel>):
    BaseAdapter() {
    override fun getCount(): Int {
        return serviceList.size
    }

    override fun getItem(position: Int): Any {
        return serviceList.get(position)
    }

    override fun getItemId(position: Int): Long {
         return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.service_cardview, null)
        val icon = view.findViewById<ImageView>(R.id.serviceImg)
        val title = view.findViewById<TextView>(R.id.serviceName)

        val listItem: ServiceModel = serviceList.get(position)

        icon.setImageResource(listItem.icons!!)
        title.text = listItem.name

        return view
    }

}