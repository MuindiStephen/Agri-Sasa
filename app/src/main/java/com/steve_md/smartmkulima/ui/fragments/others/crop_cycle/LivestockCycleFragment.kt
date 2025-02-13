package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentLivestockCycleBinding

class LivestockCycleFragment : Fragment() {
    private lateinit var binding: FragmentLivestockCycleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLivestockCycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}