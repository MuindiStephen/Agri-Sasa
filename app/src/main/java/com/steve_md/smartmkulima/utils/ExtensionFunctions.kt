package com.steve_md.smartmkulima.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException


fun Fragment.toast(text:String) {
    Toast.makeText(
        requireContext(),
        text,
        Toast.LENGTH_LONG
    ).show()
}

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun Fragment.hideKeyboard(): Boolean {
    return (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow((activity?.currentFocus ?: View(context)).windowToken, 0)
}

fun Fragment.snackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_INDEFINITE)
        .setAction("TRY AGAIN") {
            onStart()
        }
        .show()
}

fun View.snackBar(text: String,view: View) {
    Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
        .show()
}

fun Fragment.displaySnackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        .show()
}

fun Activity.displaySnackBar(text: String) {
    val parentLayout: View = findViewById(android.R.id.content)
    Snackbar.make(parentLayout, text, Snackbar.LENGTH_SHORT)
        .show()
}

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Unknown Error Occurred")
    }
}

sealed class ResourceNetwork<out T> {
    data class Success<out T>(
        val value: T
    ) : ResourceNetwork<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val errorString: String?
    ) : ResourceNetwork<Nothing>()

    object Loading : ResourceNetwork<Nothing>()
}

suspend fun <T> apiRequestByResource(api: suspend () -> T): ResourceNetwork<T> {
    return withContext(Dispatchers.IO) {
        try {
            ResourceNetwork.Success(api.invoke())

        } catch (throwable: Throwable) {
            //Timber.e("HttpException  throwable.message() ${throwable.message}")
            if (throwable is HttpException) {
//                    Timber.e(
//                        "HttpException  throwable.response() ${
//                            throwable.response()
//                        }"



                // ---------------------------------------------------------------------

                val error = throwable.response()?.errorBody()!!.string()
                val message = StringBuilder()

                error.let {
                    try {
                        message.append(JSONObject(it).getString("error_description"))
                    } catch (_: JSONException) {
                    }
                    message.append("\n")
                }

                // Timber.e("BASE REPOSITORY----->>>>>>>>>>>>>>>>>> $message")
                // ---------------------------------------------------------------------
                ResourceNetwork.Failure(
                    false,
                    throwable.code(),
                    throwable.response()?.errorBody(),
                    error
                )
            } else {
                ResourceNetwork.Failure(true, null, null, "NO NETWORK FOUND")
            }
        }
    }
}


fun Fragment.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}


fun Fragment.hideSupportActionBar() {
    (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    val layoutparams = view!!.layoutParams as FrameLayout.LayoutParams
    layoutparams.updateMargins(top = 0)
}

fun Fragment.isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

fun formatNameFromEmail(email: String): String {
    // Extract the part before the "@" symbol
    val namePart = email.substringBefore("@")

    // Split the name part by common delimiters (., _, or numbers)
    val splitName = namePart.split(".", "_", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    // Capitalize each part of the name and join them with a space
    return splitName.joinToString(" ") { part -> part.capitalize() }
}





