package com.steve_md.smartmkulima.utils


object Constants {

    // Authentication Endpoints
    const val BASE_URL = "https://shamba-app-onboarding.herokuapp.com"
    const val REGISTER_END_POINT = "/signup/email"
    const val LOGIN_END_POINT = "/login/email"
    const val CONFIRM_REGISTRATION_END_POINT = "/signup/confirm"





    const val FARM_PRODUCE_BASE_URL = "https://path.com/"

    const val CONNECT_TIMEOUT = 60 * 1000

    const val READ_TIMEOUT = 60 * 1000

    const val WRITE_TIMEOUT = 60 * 1000

    // Sandbox Env
    const val SANDBOX_BASE_URL = "https://sandbox.safaricom.co.ke/"

    // Production Env
    const val Production_BASE_URL = "https://api.safaricom.co.ke/"

    const val BUSINESS_SHORT_CODE = "174379"
    const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

    const val PARTYB = "174379" // The party receiving the funds

    const val CALLBACKURL = "http://mpesa-requestbin.herokuapp.com/1fw79g11"
    object TransactionType {
        const val CustomerPayBillOnline = "CustomerPayBillOnline"
    }
}
