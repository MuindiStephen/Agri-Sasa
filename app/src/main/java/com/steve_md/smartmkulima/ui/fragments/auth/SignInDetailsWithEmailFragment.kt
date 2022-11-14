package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentRecoverPasswordWithEmailBinding


class SignInDetailsWithEmailFragment : Fragment() {

    private lateinit var binding: FragmentRecoverPasswordWithEmailBinding

override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in_details_with_email, container, false)
    }

}