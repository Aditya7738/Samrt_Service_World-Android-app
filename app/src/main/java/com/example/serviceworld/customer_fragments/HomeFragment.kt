package com.example.serviceworld.customer_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.serviceworld.CustomerBottomNavActivity
import com.example.serviceworld.R
import com.example.serviceworld.ServiceProvidersActivity
import com.example.serviceworld.adapters.GridViewAdapter
import com.example.serviceworld.databinding.FragmentHomeBinding
import com.example.serviceworld.model.ServiceModel


class HomeFragment(customerBottomNavActivity: CustomerBottomNavActivity) : Fragment(), AdapterView.OnItemClickListener {

    var context = customerBottomNavActivity

    lateinit var binding: FragmentHomeBinding

    private var serviceList: ArrayList<ServiceModel>? = null

    private var gridAdapter: GridViewAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        serviceList = ArrayList()
        serviceList = setDataList()

        gridAdapter = GridViewAdapter(context, serviceList!!)
        binding.serviceListGV.adapter = gridAdapter

        binding.serviceListGV.onItemClickListener = this



        return binding.root
    }

    private fun setDataList(): ArrayList<ServiceModel>{
        val arrayList: ArrayList<ServiceModel> = ArrayList()
        arrayList.add(ServiceModel(R.drawable.baseline_plumbing_24, "Plumbing service"))
        arrayList.add(ServiceModel(R.drawable.shovel_svgrepo_com, "Mestri service"))
        arrayList.add(ServiceModel(R.drawable.baseline_car_repair_24, "Car repair service"))
        arrayList.add(ServiceModel(R.drawable.painter_svgrepo_com, "Wall painter service"))
        arrayList.add(ServiceModel(R.drawable.electrician, "Electrician service"))
        arrayList.add(ServiceModel(R.drawable.saw_svgrepo_com, "Carpainter service"))
        arrayList.add(ServiceModel(R.drawable.baseline_local_car_wash_24, "Car wash service"))
        arrayList.add(ServiceModel(R.drawable.welder_svgrepo_com, "Welding service"))
        return arrayList
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View?, position: Int, id: Long) {
        val item: ServiceModel = serviceList!!.get(position)

        startActivity(Intent(requireContext(), ServiceProvidersActivity::class.java).putExtra("selectedService", item.name))

    }
}