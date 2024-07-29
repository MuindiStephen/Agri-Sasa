package com.steve_md.smartmkulima.utils.form_validation.ui_extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.form_validation.ui_extensions.getPasswordError
import com.ekenya.rnd.common.form_validation.ui_extensions.isComplexPassword
import com.ekenya.rnd.common.form_validation.ui_extensions.isComplexPin
import com.ekenya.rnd.common.form_validation.ui_extensions.isValidEmail
import com.ekenya.rnd.common.form_validation.ui_extensions.removeNonDigits
import com.ekenya.rnd.common.form_validation.util.SimpleTextWatcher
import java.text.DecimalFormat
import java.text.NumberFormat

private fun TextView.doAfterTextChanged(block: (s: String) -> Unit) : SimpleTextWatcher{

    val simpleTextWatcher: SimpleTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            block.invoke(s.toString())
        }
    }

    addTextChangedListener(simpleTextWatcher)

    return simpleTextWatcher
}


fun TextView.isNotEmpty(): LiveData<Boolean> {

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->
        val isValid = newValue.isNotEmpty()
        subject.value = isValid
        error = if (isValid) null else {
            "Input should not be empty"
        }
    }
    return subject
}

fun TextView.isValidEmail(): LiveData<Boolean> {

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->

        val isValid = newValue.isValidEmail()

        subject.value = isValid

        error = if (isValid) null else {
            "Invalid Email"
        }
    }
    return subject
}

fun TextView.isValidPin(): LiveData<Boolean> {

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->

        val isValid = newValue.isComplexPin()

        subject.value = isValid

        error = if (isValid) null else {
            "Please try another complex Pin"
        }
    }
    return subject
}

fun TextView.validateMinimumLength(len: Int): LiveData<Boolean> {

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->

        val isValid = newValue.length >= len

        subject.value = isValid

        error = if (isValid) null else {
            "Input should not be less than $len"
        }
    }
    return subject
}


fun TextView.passwordsMatch(textView: TextView): LiveData<Boolean> {

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->

        val prev = textView.text.toString()
        val isValid = newValue == prev

        subject.value = isValid

        error = if (isValid) null else {
            "Passwords Do not match"
        }
    }
    return subject
}

fun TextView.isValidPassword() : LiveData<Boolean>{

    val subject = MutableLiveData(false)

    doAfterTextChanged { newValue ->

        val isValid =  newValue.isComplexPassword()

        subject.value = isValid

        error = if (isValid) null else {
            newValue.getPasswordError()
        }
    }
    return subject
}

fun EditText.formatAsCurrency() {

    addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // do nothing
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {

            val input = editable.toString().removeNonDigits()

            if (input.isEmpty()){
                return
            }

            val formatter: NumberFormat = DecimalFormat("#,###")

            var formattedNumber: String = formatter.format(input.toInt()).toString()
            formattedNumber = "Kes $formattedNumber"

            removeTextChangedListener(this)
            setText(formattedNumber)
            setSelection(formattedNumber.length)
            addTextChangedListener(this)
        }
    })

}

//fun Button.validateForm(
//    validators: List<LiveData<Boolean>>,
//    skipFirstEvents: Boolean = false,
//    fireIfAtLeastOneIsValid: Boolean = false,
//) {
//    ButtonObserverEngine(
//        lifecycleOwner = LifecycleOwner().get(this),
//        validators = validators,
//        skipFirstEvents = skipFirstEvents,
//        fireIfAtLeastOneIsValid = fireIfAtLeastOneIsValid
//    ) { enable ->
//        isEnabled = enable
//    }
//}