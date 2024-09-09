package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentMyCartAgroDealerInputsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyCartAgroDealerInputsFragment : Fragment() {

    private lateinit var binding: FragmentMyCartAgroDealerInputsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMyCartAgroDealerInputsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {

    }
}