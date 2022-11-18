package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentMainAuthsBinding


class AuthsMainFragment : Fragment() {

    private lateinit var binding: FragmentMainAuthsBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainAuthsBinding.inflate(layoutInflater, container, false)



        binding.alreadyHaveAccountLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_authsMainFragment_to_loginMainFragment)
        }

        binding.usePhoneEmailButton.setOnClickListener {
            // findNavController().navigate(R.id.aut)
        }



        val view = binding.root
        navController = findNavController()

//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)


        return view
    }

}