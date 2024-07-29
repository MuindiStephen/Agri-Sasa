package com.steve_md.smartmkulima.utils

sealed class PaymentOptionState {
    object MobileMoney : PaymentOptionState()
    object BankAccount : PaymentOptionState()
    object DebitCredit : PaymentOptionState()
    object NFC : PaymentOptionState()
    object PayForMe : PaymentOptionState()
    object AddNewCard : PaymentOptionState()
}
