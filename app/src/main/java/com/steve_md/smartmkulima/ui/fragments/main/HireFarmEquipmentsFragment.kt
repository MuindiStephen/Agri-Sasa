package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentHireFarmEquipmentsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HireFarmEquipmentsFragment : Fragment() {

    private lateinit var binding:FragmentHireFarmEquipmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHireFarmEquipmentsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}