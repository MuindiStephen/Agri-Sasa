package com.steve_md.smartmkulima.payment.mpesa

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushSuccessResponse
import com.steve_md.smartmkulima.ui.fragments.main.PaymentFragment


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("MessagingService", remoteMessage.data.toString())

        val payload = remoteMessage.data["payload"]

        val gson = Gson()

        val mpesaResponse: StkPushSuccessResponse = gson.fromJson(payload, StkPushSuccessResponse::class.java)

        Log.e("MessagingServiceSecond", mpesaResponse.toString())

        val id = mpesaResponse.body.stkCallback.checkoutRequestID

        if (mpesaResponse.body.stkCallback.resultCode != 0) {

            val reason = mpesaResponse.body.stkCallback.resultDesc
            PaymentFragment.mpesaListener.sendFailed(reason)
            Log.d("MessagingServiceThird", "Operation Failed")
        } else {
            Log.d("MessagingServiceThird", "Operation Success")

            val list = mpesaResponse.body.stkCallback.callbackMetadata.item

            var receipt = ""
            var date = ""
            var phone = ""
            var amount = ""


            for (item in list) {

                if (item.name == "MpesaReceiptNumber") {
                    receipt = item.value.toString()
                }
                if (item.name == "TransactionDate") {
                    date = item.value.toString()
                }
                if (item.name == "PhoneNumber") {
                    phone = item.value.toString()

                }
                if (item.name == "Amount") {
                    amount = item.value.toString()
                }

            }
            PaymentFragment.mpesaListener.sendSuccesfull(amount, phone, date, receipt)
            Log.d("MetaData", "\nReceipt: $receipt\nDate: $date\nPhone: $phone\nAmount: $amount")
            //Log.d("NewDate", getDate(date.toLong()))
        }

        FirebaseMessaging.getInstance().unsubscribeFromTopic(id)
        /*
        if (id != null) {
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(id)
        }*/

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}