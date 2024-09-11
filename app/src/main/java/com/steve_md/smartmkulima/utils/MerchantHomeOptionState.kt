package com.steve_md.smartmkulima.utils

sealed class MerchantHomeOptionState {
    data object DirectPayment : MerchantHomeOptionState()
    data object ScanAndPay : MerchantHomeOptionState()
    data object HotDeals : MerchantHomeOptionState()
    data object EventsAndTickets : MerchantHomeOptionState()

}

