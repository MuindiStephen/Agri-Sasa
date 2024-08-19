package com.steve_md.smartmkulima.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.AgroDealer

class AgrodealerAdapter(
    private val agrodealers: List<AgroDealer>,
    private val userLocation: LatLng,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<AgrodealerAdapter.AgrodealerViewHolder>() {

    class AgrodealerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.agrodealer_name)
        val contactTextView: TextView = view.findViewById(R.id.textViewPhone)
        val tvEmail: TextView = view.findViewById(R.id.textViewEmail)
        val distanceTextView : TextView = view.findViewById(R.id.tvLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgrodealerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agrodealer, parent, false)
        return AgrodealerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgrodealerViewHolder, position: Int) {
        val agrodealer = agrodealers[position]
        holder.nameTextView.text = agrodealer.name
        holder.contactTextView.text = agrodealer.phone
        holder.tvEmail.text = agrodealer.email

        // Calculate the distance between the user's location and the agrodealer
        val agrovetLatLng = LatLng(agrodealer.latitude, agrodealer.longitude)
        val distance = calculateDistance(userLocation, agrovetLatLng)
        holder.distanceTextView.text = String.format("%.2f km away", distance / 1000)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(agrodealer = agrodealer)
        }
    }

    override fun getItemCount(): Int = agrodealers.size

    private fun calculateDistance(startLatLng: LatLng, endLatLng: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            startLatLng.latitude, startLatLng.longitude,
            endLatLng.latitude, endLatLng.longitude,
            results
        )
        return results[0]
    }

    class OnClickListener(val clickListener: (agrodealer: AgroDealer) -> Unit) {
        fun onClick(agrodealer: AgroDealer) = clickListener(agrodealer)
    }
}
