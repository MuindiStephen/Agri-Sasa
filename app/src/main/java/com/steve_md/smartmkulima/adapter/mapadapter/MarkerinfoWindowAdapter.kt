package com.steve_md.smartmkulima.adapter.mapadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.steve_md.smartmkulima.databinding.MarkerInfoContentsBinding
import com.steve_md.smartmkulima.model.mapmodels.Place

/**
 * Give info about a place( : ) : ( : ) : ( : )
 */
class MarkerinfoWindowAdapter (
    private val context: Context
): GoogleMap.InfoWindowAdapter {

    lateinit var binding: MarkerInfoContentsBinding
    @SuppressLint("SetTextI18n")
    override fun getInfoContents(marker: Marker): View? {
        val place = marker.tag as? Place ?: return null

        binding = MarkerInfoContentsBinding.inflate(
            LayoutInflater.from(context) )

        binding.tvTitle.text = place.name
        binding.tvAddress.text = place.address
        binding.tvRating.text = "Rating: %.2f".format(place.rating)

        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

}