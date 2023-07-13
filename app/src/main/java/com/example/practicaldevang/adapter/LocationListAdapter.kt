package com.example.practicaldevang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaldevang.R
import com.example.practicaldevang.data.DataLoc

class LocationListAdapter(
    val locClickDeleteInterface: LocClickDeleteInterface,
    val locClickInterface: LocClickInterface
) :
    RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {

    private val allLocations = ArrayList<DataLoc>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        val imgEdit: ImageView = itemView.findViewById(R.id.imgEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row_location_data,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = allLocations[position].title
        holder.textView2.text = allLocations[position].address
        holder.imgDelete.setOnClickListener {
            locClickDeleteInterface.onDeleteIconClick(allLocations[position])
        }
        holder.imgEdit.setOnClickListener {
            locClickInterface.onLocClick(allLocations[position])
        }
    }

    override fun getItemCount(): Int {
        return allLocations.size
    }

    fun updateList(newList: List<DataLoc>) {
        allLocations.clear()
        allLocations.addAll(newList)
        notifyDataSetChanged()
    }
}

interface LocClickDeleteInterface {
    fun onDeleteIconClick(dataLoc: DataLoc)
}

interface LocClickInterface {
    fun onLocClick(dataLoc: DataLoc)
}