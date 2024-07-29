package com.steve_md.smartmkulima.utils

import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.utils.Constants.DIRECT_PAYMENT
import com.steve_md.smartmkulima.utils.Constants.EVENTS_TICKETS
import com.steve_md.smartmkulima.utils.Constants.HOT_DEALS
import com.steve_md.smartmkulima.utils.Constants.SCAN_PAY
import java.util.ArrayList

data class MyCards(
    val cardType: String,
    val cardNumber: String,
    val name: String,
    val visa: Int,
    val expiry: String
)

data class MerchantOptions(val icon: Int, val title: String, val subtitle: String)
data class PaymentOption(val image: Int, val name: String)
class Options(
    val optionString: String,
    val optionImage: Int
) {
    override fun toString(): String {
        return optionString
    }
}

object MerchantListUtils {
    val merchantPaymentOptions = mutableListOf<MerchantOptions>().apply {
        add(
            MerchantOptions(
                R.drawable.ic_direct_payment_merchant,
                DIRECT_PAYMENT,
                "Make direct merchant payments"
            )
        )
        add(
            MerchantOptions(
                R.drawable.ic_scan_pay_merchant,
                SCAN_PAY,
                "Scan merchant QR Code to pay"
            )
        )
        add(
            MerchantOptions(
                R.drawable.ic_events_tickets_merchant,
                EVENTS_TICKETS,
                "See all events and get yourself tickets"
            )
        )
        add(
            MerchantOptions(
                R.drawable.ic_hot_deals_merchant,
                HOT_DEALS,
                "Make purchases on deals and cash backs"
            )
        )
    }

    val paymentOptions = mutableListOf<PaymentOption>().apply {
        add(PaymentOption(R.drawable.ic_money_mobile_merchant, "Mobile \nMoney"))
        add(PaymentOption(R.drawable.ic_bank_account_merchant, "Bank \nAccount"))
        add(PaymentOption(R.drawable.ic_debit_credit_merchant, "Debit\\ \nCredit"))
        add(PaymentOption(R.drawable.ic_nfc_merchant, "(NFC)"))
    }

    val spinnerPayments = ArrayList<Options>().apply {
        add(Options("Mpesa", R.drawable.mpesa_merchant))
        add(Options("Airtel", R.drawable.airtel))
    }

    val myCards = mutableListOf<MyCards>().apply {

        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )
        add(
            MyCards(
                "Gold Credit",
                "1213 •••• •••• 5083",
                "Alex Wanjohi",
                R.drawable.ic_visa_merchant,
                "12/25"
            )
        )

    }

}

