package com.ekenya.rnd.merchant.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Model the contacts
 */

@Parcelize
data class ContactModel (
    var name: String,
    var phone: String,
    val initials: String
) : Parcelable