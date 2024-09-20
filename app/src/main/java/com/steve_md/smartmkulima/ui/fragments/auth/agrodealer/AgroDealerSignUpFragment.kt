package com.steve_md.smartmkulima.ui.fragments.auth.agrodealer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAgroDealerSignUpBinding

class AgroDealerSignUpFragment : Fragment() {

    private lateinit var binding: FragmentAgroDealerSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAgroDealerSignUpBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}