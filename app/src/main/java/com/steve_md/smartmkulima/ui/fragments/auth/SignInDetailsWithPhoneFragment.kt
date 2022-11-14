package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentSignInDetailsWithPhoneBinding

class SignInDetailsWithPhoneFragment : Fragment() {

    private lateinit var binding: FragmentSignInDetailsWithPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInDetailsWithPhoneBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

}