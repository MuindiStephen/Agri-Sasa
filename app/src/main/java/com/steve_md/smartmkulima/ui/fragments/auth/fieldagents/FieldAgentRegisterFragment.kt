package com.steve_md.smartmkulima.ui.fragments.auth.fieldagents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentFieldAgentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FieldAgentRegisterFragment : Fragment() {
    private var _binding: FragmentFieldAgentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFieldAgentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.tvHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.fieldAgentLoginFragment)
        }

    }
}