package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithEmailBinding


class SignUpDetailsWithEmailFragment : Fragment() {
    private lateinit var binding: FragmentSignUpDetailsWithEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpDetailsWithEmailBinding.inflate(layoutInflater, container, false)

        binding.signUpWithPhoneInsteadText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_signUpDetailsMainFragment)
        }
        binding.buttonSignUpWithEmail.setOnClickListener {
            findNavController().navigate(R.id.action_signUpDetailsMainFragment_to_verificationFragment)
        }

        return binding.root
    }

}