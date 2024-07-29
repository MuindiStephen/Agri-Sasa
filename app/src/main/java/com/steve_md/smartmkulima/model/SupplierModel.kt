package com.steve_md.smartmkulima.model

import com.google.android.gms.maps.model.LatLng

/**
 * SupplierModel based on their services, categories and leasing options
 */
data class Supplier(
    val id: String,
    val name: String,
    val services: List<String>,
    val categories: List<String>,
    val leasingOptions: Boolean,
    val leaseDetails: LeaseDetails?,
    val location: LatLng
)

data class LeaseDetails(
    val leaseDuration: String, // e.g., "6 months", "1 year"
    val paymentTerms: String, // e.g., "Monthly", "Quarterly"
    val maintenanceIncluded: Boolean
)

