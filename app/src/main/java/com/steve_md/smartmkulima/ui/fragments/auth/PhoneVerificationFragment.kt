package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentPhoneVerificationBinding


class PhoneVerificationFragment : Fragment() {

    private lateinit var binding: FragmentPhoneVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPhoneVerificationBinding.inflate(layoutInflater, container, false)


        binding.emailVerificationCodeText.setOnClickListener {
            findNavController().navigate(R.id.action_verificationFragment_to_emailVerificationFragment)
        }
        return binding.root
    }


}