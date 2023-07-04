package com.steve_md.smartmkulima.utils

import android.content.Context
import android.content.SharedPreferences


/**
 * Handle Shared preferences to store and retrieve user token
 */
object SharedPreferences {
    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_USER_TOKEN = "userToken"

    private fun getSharedPreferences(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    }

    fun setUserToken(context: Context, token: String) {
        val editor = getSharedPreferences(context)?.edit()
        editor?.putString(KEY_USER_TOKEN, token)
        editor?.apply()
    }

    fun getUserToken(context: Context): String? {
        return getSharedPreferences(context)?.getString(KEY_USER_TOKEN, null)
    }
}

/** Step 2
 * // After successful login API response
val token = "your_token_from_api_response"
PreferenceManager.setUserToken(context, token)
 */

/** Step 3
 * class SplashFragment : Fragment() {

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
super.onViewCreated(view, savedInstanceState)

val userToken = PreferenceManager.getUserToken(requireContext())

if (userToken != null) {
// User is logged in, navigate to home fragment
findNavController().navigate(R.id.action_splash_to_home)
} else {
// User is not logged in, navigate to login fragment
findNavController().navigate(R.id.action_splash_to_login)
}
}
}
In this example, PreferenceManager.getUserToken() is used to retrieve the user token from shared preferences. If the token is not null, it means the user is logged in, and you can navigate directly to the home fragment. Otherwise, navigate to the login fragment.

Remember to adapt the code to match your specific authentication logic, API response handling, and navigation IDs in the navigation graph.

Additionally, make sure to handle token expiration and authentication state changes appropriately to keep the user's login session secure.







 *
 */