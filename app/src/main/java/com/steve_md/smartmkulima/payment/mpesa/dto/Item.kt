package com.steve_md.smartmkulima.payment.mpesa.dto


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: Double
)


/**
 *
 * @sample success response for this Transaction
{
        "Body": {
        "stkCallback": {
        "MerchantRequestID": "29115-34620561-1",
        "CheckoutRequestID": "ws_CO_191220191020363925",
        "ResultCode": 0,
        "ResultDesc": "The service request is processed successfully.",
        "CallbackMetadata": {
        "Item": [{
        "Name": "Amount",
        "Value": 1.00
        },
        {
        "Name": "MpesaReceiptNumber",
        "Value": "NLJ7RT61SV"
        },
        {
        "Name": "TransactionDate",
        "Value": 20191219102115
        },
        {
        "Name": "PhoneNumber",
        "Value": 254708374149
        }]
        }
        }
        }
}

 */