package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentLoginMainBinding


class LoginMainFragment : Fragment() {

    private lateinit var binding: FragmentLoginMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentLoginMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dontHaveAccountSignUpText.setOnClickListener {
            findNavController().navigate(R.id.action_loginMainFragment_to_signUpDetailsMainFragment)
        }

        binding.usePhoneEmailButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginMainFragment_to_signInDetailsWithEmailFragment)
        }
    }

}