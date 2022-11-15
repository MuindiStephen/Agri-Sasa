package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentEmailVerificationBinding


class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentEmailVerificationBinding.inflate(layoutInflater, container, false)

        binding.phoneVerificationCodeText.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerificationFragment_to_verificationFragment)
        }

        binding.imageView2.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsFragment)
        }

        return binding.root
    }


}