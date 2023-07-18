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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint


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


        val userIsLoggedIn = FirebaseAuth.getInstance().currentUser


        Handler(Looper.getMainLooper()).postDelayed({

            if (userIsLoggedIn != null) {
                findNavController().navigate(R.id.action_splashFragment_to_homeDashboardFragment2)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authsMainFragment)
            }
        }, 3000)

    }
}