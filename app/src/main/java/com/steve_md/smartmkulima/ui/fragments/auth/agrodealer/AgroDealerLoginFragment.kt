package com.steve_md.smartmkulima.ui.fragments.auth.agrodealer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentAgroDealerLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AgroDealerLoginFragment : Fragment() {
    private lateinit var binding: FragmentAgroDealerLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAgroDealerLoginBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}