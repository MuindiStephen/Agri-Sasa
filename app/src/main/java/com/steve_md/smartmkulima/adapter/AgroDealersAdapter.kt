package com.steve_md.smartmkulima.adapter

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.AgroDealer

/**
 * Attach / bind data of verified agro-dealers within Agri-Sasa Platform
 */
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
        val viewAgroDealerMoreDetailsTV : TextView = view.findViewById(R.id.tvViewMoreDetailAboutAgroDealer)
        val itemAG: ConstraintLayout = view.findViewById(R.id.itemAG)
        val agroDealerBuildingLoc: TextView = view.findViewById(R.id.textViewBuilding)
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
        holder.agroDealerBuildingLoc.text = agrodealer.buildingLocation


        // Calculate the distance between the user's location and the agrodealer
        val agrovetLatLng = LatLng(agrodealer.latitude, agrodealer.longitude)
        val distance = calculateDistance(userLocation, agrovetLatLng)
        holder.distanceTextView.text = String.format("%.2f km away", distance / 1000)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(agrodealer = agrodealer, distance = String.format("%.2f km away", distance / 1000))
        }

        holder.itemAG.setOnClickListener {
            onClickListener.onClick(agrodealer = agrodealer, distance = String.format("%.2f km away", distance / 1000))
        }

        holder.viewAgroDealerMoreDetailsTV.setOnClickListener {
            onClickListener.onClick(agrodealer = agrodealer, distance = String.format("%.2f km away", distance / 1000))
        }

        holder.distanceTextView.setOnClickListener {
            openLocationInGoogleMaps(holder.itemView.context, agrovetLatLng)
        }

        holder.contactTextView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL // Action for what intent called for
            intent.data = Uri.parse("tel: ${holder.contactTextView.text}") // Data with intent respective action on intent
            holder.itemView.context.startActivity(intent)
        }

        holder.tvEmail.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"")
            holder.itemView.context.startActivity(Intent.createChooser(intent,"Open With"))
        }
    }

    private fun openLocationInGoogleMaps(context: Context?, agrovetLatLng: LatLng) {
        val uri = "geo:${agrovetLatLng.latitude},${agrovetLatLng.longitude}?q=${agrovetLatLng.latitude},${agrovetLatLng.longitude}(Agrodealer)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        // context!!.startActivity(intent)
        if (context != null) {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_LONG).show()
            }
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

    class OnClickListener(val clickListener: (agrodealer: AgroDealer,distance: String) -> Unit) {
        fun onClick(agrodealer: AgroDealer, distance: String) = clickListener(agrodealer, distance)
    }
}
