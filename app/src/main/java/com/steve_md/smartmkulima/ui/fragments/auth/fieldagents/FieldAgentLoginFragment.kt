package com.steve_md.smartmkulima.ui.fragments.auth.fieldagents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentFieldAgentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FieldAgentLoginFragment : Fragment() {

    private var _binding: FragmentFieldAgentLoginBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFieldAgentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.fieldAgentRegisterFragment)
        }

        binding.signInFieldAgentButton.setOnClickListener {
            findNavController().navigate(R.id.fieldAgentDashboardFragment)
        }
    }
}