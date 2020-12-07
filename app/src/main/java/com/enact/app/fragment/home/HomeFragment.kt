package com.enact.app.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.enact.app.R
import com.enact.app.adapter.HomeProductAdapter
import com.enact.app.adapter.WineriesAdapter
import com.enact.app.databinding.HomeFragmentBinding

class HomeFragment :Fragment(){
    lateinit var binding: HomeFragmentBinding
    lateinit var wineriesAdapter: WineriesAdapter
    lateinit var homeProductAdapter: HomeProductAdapter
    lateinit var productArrayLis: ArrayList<String>
    lateinit var wineriesArray:ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        intiView()
        return binding.root
    }

    private fun intiView() {
        setAdapter()
    }

    private fun setAdapter() {
        wineriesArray = ArrayList()
        productArrayLis= ArrayList()

        wineriesArray.add("1")
        wineriesArray.add("1")
        wineriesArray.add("1")

        productArrayLis.add("1")
        productArrayLis.add("1")
        productArrayLis.add("1")

        wineriesAdapter = WineriesAdapter(wineriesArray,object :WineriesAdapter.WineriesAdapterListener{
            override fun onItemClickListener(clickItem: String) {

            }
        })
        binding.wineriesList.layoutManager= LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.wineriesList.adapter= wineriesAdapter

        //set Product Adapter
        binding.wineList.layoutManager = GridLayoutManager(this.requireContext(),2)
        homeProductAdapter = HomeProductAdapter(productArrayLis,object :HomeProductAdapter.HomeProductAdapterListener{
            override fun onItemClick(clickItem: String) {

            }
        })
        binding.wineList.adapter= homeProductAdapter




    }

}