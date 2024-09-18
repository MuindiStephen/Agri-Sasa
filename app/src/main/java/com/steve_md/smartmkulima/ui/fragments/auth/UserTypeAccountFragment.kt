package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentUserTypeAccountBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Select Account type could be -:
 * Either Supplier/ AgroDealer or Farmer
 */
@AndroidEntryPoint
class UserTypeAccountFragment : Fragment() {

    private var _binding: FragmentUserTypeAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserTypeAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding.apply {
           
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}