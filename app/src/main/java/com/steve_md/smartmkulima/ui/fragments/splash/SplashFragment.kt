package com.steve_md.smartmkulima.ui.fragments.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSplashBinding
import com.steve_md.smartmkulima.utils.makeStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // makeStatusBarTransparent()

        /**
         * Deprecated and leads to an exception with :)
         * java.lang.IllegalStateException: Fragment SplashFragment{4f425ca} (993704d7-7477-4650-a722-7b5ca352e26c) not associated with a fragment manager.
         */
//        Handler(Looper.getMainLooper()).postDelayed({
//
//            if (userIsLoggedIn != null) {
//                findNavController().navigate(R.id.action_splashFragment_to_homeDashboardFragment2)
//            } else {
//                findNavController().navigate(R.id.action_splashFragment_to_authsMainFragment)
//            }
//        }, 3000)

    }

    override fun onResume() {
        super.onResume()
        performNavigation()
    }

    private fun performNavigation() {
        lifecycleScope.launch {
            delay(1500L)

            val userIsLoggedIn = FirebaseAuth.getInstance().currentUser
            if (userIsLoggedIn != null) {
                findNavController().navigate(R.id.action_splashFragment_to_homeDashboardFragment2)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authsMainFragment)
            }

        }
    }
}