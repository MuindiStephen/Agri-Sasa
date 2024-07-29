package com.steve_md.smartmkulima.utils

sealed class MerchantHomeOptionState {
    object DirectPayment : MerchantHomeOptionState()
    object ScanAndPay : MerchantHomeOptionState()
    object HotDeals : MerchantHomeOptionState()
    object EventsAndTickets : MerchantHomeOptionState()

}

