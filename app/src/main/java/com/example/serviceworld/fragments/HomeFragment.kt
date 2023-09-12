package com.example.serviceworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.serviceworld.BottomNavActivity
import com.example.serviceworld.R
import com.example.serviceworld.adapters.GridViewAdapter
import com.example.serviceworld.model.ServiceModel


class HomeFragment(bottomNavActivity: BottomNavActivity) : Fragment(), AdapterView.OnItemClickListener {

    var context = bottomNavActivity

    private var serviceList: ArrayList<ServiceModel>? = null
    private var gridView: GridView? = null
    private var gridAdapter: GridViewAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_home, container, false)

        gridView = view.findViewById(R.id.serviceList)
        serviceList = ArrayList()
        serviceList = setDataList()

        gridAdapter = GridViewAdapter(context, serviceList!!)
        gridView?.adapter = gridAdapter

        gridView?.onItemClickListener = this
        return view
    }

    private fun setDataList(): ArrayList<ServiceModel>{
        val arrayList: ArrayList<ServiceModel> = ArrayList()
        arrayList.add(ServiceModel(R.drawable.baseline_plumbing_24, "Plumbing service"))
        arrayList.add(ServiceModel(R.drawable.shovel_svgrepo_com, "Mestri service"))
        arrayList.add(ServiceModel(R.drawable.baseline_car_repair_24, "Car repair service"))
        arrayList.add(ServiceModel(R.drawable.painter_svgrepo_com, "Wall painter service"))
        arrayList.add(ServiceModel(R.drawable.electrician, "Electrician"))
        arrayList.add(ServiceModel(R.drawable.saw_svgrepo_com, "Carpainter"))
        arrayList.add(ServiceModel(R.drawable.baseline_local_car_wash_24, "Car wash service"))
        arrayList.add(ServiceModel(R.drawable.welder_svgrepo_com, "Welding service"))
        return arrayList
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View?, position: Int, id: Long) {
        var item: ServiceModel = serviceList!!.get(position)

    }
}