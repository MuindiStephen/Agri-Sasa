package com.steve_md.smartmkulima.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentYourAccountBinding
import com.steve_md.smartmkulima.ui.fragments.others.Settings


class YourAccountFragment : Fragment() {

    private var _binding:FragmentYourAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourAccountBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            settings.setOnClickListener {
               startActivity(Intent(requireActivity().applicationContext, Settings::class.java))
                requireActivity().finish()
            }
        }
    }
}