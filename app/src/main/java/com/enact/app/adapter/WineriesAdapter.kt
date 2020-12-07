package com.enact.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.enact.app.R
import com.enact.app.databinding.WineriesAdapterBinding

class WineriesAdapter(arrayList: ArrayList<String>, val listener:WineriesAdapterListener) :
        RecyclerView.Adapter<WineriesAdapter.ViewHolder>() {
    private var arrayList: ArrayList<String>

    init {
        setHasStableIds(true)
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: WineriesAdapterBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.wineries_adapter, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])



    }

    fun notifyAdapter(resultList: ArrayList<String>) {
        arrayList = resultList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    class ViewHolder(val binding: WineriesAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface WineriesAdapterListener{
        fun onItemClickListener(clickItem:String)
    }


}