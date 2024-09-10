package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentMakePaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakePaymentFragment : Fragment() {

    private lateinit var binding: FragmentMakePaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMakePaymentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.payToolbar.setupWithNavController(navController, appBarConfiguration)

    }
}