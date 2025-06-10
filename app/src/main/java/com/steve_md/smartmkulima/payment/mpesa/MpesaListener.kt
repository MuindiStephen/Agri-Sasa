package com.steve_md.smartmkulima.payment.mpesa

interface MpesaListener {
    fun sendSuccesfull(amount: String, phone: String, date: String, receipt: String)
    fun sendFailed(reason: String)
}