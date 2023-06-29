package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.AgriTechCompany
import timber.log.Timber


class LocateAgriTechCompaniesFragment : Fragment() {

     private lateinit var map: GoogleMap
     private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        val agriTechCompanies = getAgriTechCompaniesData()

        for (agriTechCompany in agriTechCompanies) {
            val location = LatLng(agriTechCompany.longitude, agriTechCompany.latitude)
            googleMap.addMarker(MarkerOptions().position(location).title(agriTechCompany.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))

            Timber.tag(TAG).i("AgriTech Companies available %s", agriTechCompany)
        }

        enableMyLocation()

        if (agriTechCompanies.isNotEmpty()) {
            val firstLocation = LatLng(agriTechCompanies[0].latitude, agriTechCompanies[0].longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 18f))
        }

    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(), ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    private fun getAgriTechCompaniesData(): List<AgriTechCompany> {
        return listOf(
            AgriTechCompany("SIKATA AGRITECH FARMERS CHOICE", 0.5929, 34.5429839),
            AgriTechCompany("ROSE AGRITECH", 0.5929, 34.5429839),
            AgriTechCompany("JOSEMO AGRITECH", 0.565095, 34.5431684),
            AgriTechCompany("OMUSALE AGRITECH", 0.565095, 34.5431684),
            AgriTechCompany("MULTIDUSH AGRITECH", 0.565095, 34.5431684)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_locate_agri_tech_companies, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
            Timber.tag(TAG).i(it.name)
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapLocateAgritechCompany) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
//                enableMyLocation()
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }
    companion object {
        const val TAG = "LocateAgriTechCompaniesFragment"
    }
}