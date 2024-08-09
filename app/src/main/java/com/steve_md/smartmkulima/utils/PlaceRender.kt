package com.steve_md.smartmkulima.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.mapmodels.Place

class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager), Parcelable {

    /**
     * The icon to use for each cluster item
     */
    private val houseIcon: BitmapDescriptor = run {
        val color = ContextCompat.getColor(
            context,
            R.color.colorPrimary
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.baseline_warehouse_24,
            color
        )
    }

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("map"),
        TODO("clusterManager")
    ) {
    }

    /**
     * Before the cluster item (the marker) is rendered.
     * Assign location on map.
     */
    override fun onBeforeClusterItemRendered(
        item: Place,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.name)
            .position(item.latLng)
            .icon(houseIcon)
    }

    /**
     * After the cluster item (the marker) is rendered.
     * Properties location on map.
     */
    override fun onClusterItemRendered(
        clusterItem: Place,
        marker: Marker
    ) {
        marker.tag = clusterItem
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceRenderer> {
        override fun createFromParcel(parcel: Parcel): PlaceRenderer {
            return PlaceRenderer(parcel)
        }

        override fun newArray(size: Int): Array<PlaceRenderer?> {
            return arrayOfNulls(size)
        }
    }
}