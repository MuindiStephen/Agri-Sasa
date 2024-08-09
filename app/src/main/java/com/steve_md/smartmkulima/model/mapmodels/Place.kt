package com.steve_md.smartmkulima.model.mapmodels

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.clustering.ClusterItem
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.mapmodels.responses.PlaceResponse
import com.steve_md.smartmkulima.model.mapmodels.responses.toPlace
import java.io.InputStream
import java.io.InputStreamReader

data class Place(
    val name: String,
    val latLng: LatLng,
    val address: String,
    val rating: Float
): ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String {
        return address
    }

}

class PlacesReader(private val context: Context) {

    private val gson = Gson()

    private val inputStream: InputStream
        get() = context.resources.openRawResource(R.raw.places)

    fun read(): List<Place> {
        val itemType = object : TypeToken<List<PlaceResponse>>() {}.type
        val reader = InputStreamReader(inputStream)
        return gson.fromJson<List<PlaceResponse>>(reader, itemType).map {
            it.toPlace()
        }
    }
}