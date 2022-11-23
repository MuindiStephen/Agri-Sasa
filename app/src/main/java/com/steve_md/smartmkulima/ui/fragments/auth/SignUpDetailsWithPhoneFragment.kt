package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithPhoneBinding


class SignUpDetailsWithPhoneFragment : Fragment() {

    private lateinit var navController: NavController

    private lateinit var binding: FragmentSignUpDetailsWithPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpDetailsWithPhoneBinding.inflate(layoutInflater, container, false)

        binding.signUpWithEmailInsteadText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpDetailsMainFragment_to_signUpDetailsWithEmailFragment)
        }
        binding.buttonSignUpWithPhone.setOnClickListener {
            findNavController().navigate(R.id.action_signUpDetailsMainFragment_to_verificationFragment)
        }
        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.mainAuthsToolbar.title = null

        return binding.root
    }



}