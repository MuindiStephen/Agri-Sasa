package com.ekenya.rnd.common.form_validation.ui_extensions

import android.util.Patterns
import java.util.regex.Pattern

// checks if an email is valid
fun String.isValidEmail(): Boolean {
    return isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// checks if a password is complex
fun String.isComplexPassword() : Boolean{
    val passwordREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$");
    return passwordREGEX.matcher(this).matches()
}

// returns what is making the password not complex
fun String.getPasswordError() : String {
    return when {
        !contains(Regex("[0-9]")) -> "Password must contain at least one digit"
        !contains(Regex("[a-z]")) -> "Password must contain at least lower case letter"
        !contains(Regex("[A-Z]")) -> "Password must contain at least one capital letter"
        !contains(Regex("[A-Z]")) -> "Password must contain at least 1 special character"
        !contains(Regex("[@#\$%^&+=]")) -> "Password must contain at least 1 special character"
        !contains(Regex("[\\S+]")) -> "Password must contain no white spaces"
        !contains(Regex(".{8,}")) -> "Password must contain at least 8 characters"
        !contains(Regex("[^a-zA-Z0-9 ]")) -> "Password must contain at least one special character"
        else -> "Password too simple"
    }
}

//checks to see why a pin is not complex
fun String.isComplexPin(): Boolean {

    if (this.isEmpty()) return false
    var previousChar = this[0]
    var count = 0;
    for (i in 1 until this.length) {
        val currentChar = this[i]
        if (currentChar == previousChar + 1 || currentChar == previousChar - 1) {
            ++count
        }
        previousChar = currentChar
    }
    val digits = this.toCharArray().map { it.toString().toInt() }
    return count < 2 && this.length == 4 && digits.distinct().size > 2 && this != "2580"
}

fun String.removeNonDigits() : String {
    return this.replace("[^0-9]".toRegex(), "")
}

fun String.formatAsHiddenCardNumber(): String {
    return this.substring(0,4) + " •••• •••• " + this.substring(this.length-5 , this.length)
}
