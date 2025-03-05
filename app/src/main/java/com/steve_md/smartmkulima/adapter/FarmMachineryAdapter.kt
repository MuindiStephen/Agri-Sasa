package com.steve_md.smartmkulima.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.FarmMachinery
import java.io.File

class FarmMachineryAdapter(private val machineryList: List<FarmMachinery>) :
    RecyclerView.Adapter<FarmMachineryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.machineryImage)
        val nameView: TextView = itemView.findViewById(R.id.machineryName)
        val descView: TextView = itemView.findViewById(R.id.machineryDescription)
        val priceView: TextView = itemView.findViewById(R.id.machineryPrice)
        val syncStatus: TextView = itemView.findViewById(R.id.syncStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_farm_machinery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val machinery = machineryList[position]
        holder.nameView.text = machinery.name
        holder.descView.text = machinery.description
        holder.priceView.text = "Ksh ${machinery.price}"

        // Load Image
        Glide.with(holder.itemView.context)
            .load(File(machinery.productImage)) // Change if using URI/Base64
            .into(holder.imageView)

        // Sync Status
        if (machinery.isSynced) {
            holder.syncStatus.text = "Synced"
            holder.syncStatus.setBackgroundColor(Color.GREEN)
        } else {
            holder.syncStatus.text = "Unsynced"
            holder.syncStatus.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount() = machineryList.size
}
