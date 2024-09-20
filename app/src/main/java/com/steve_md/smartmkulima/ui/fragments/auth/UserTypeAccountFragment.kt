package com.steve_md.smartmkulima.ui.fragments.auth

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentUserTypeAccountBinding
import com.steve_md.smartmkulima.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Select Account type could be -:
 * Either Supplier/ AgroDealer or Farmer
 */
@AndroidEntryPoint
class UserTypeAccountFragment : Fragment() {

    private var _binding: FragmentUserTypeAccountBinding? = null
    private val binding get() = _binding!!
    private var roleName = "Farmer"

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
            cvAgrodealer.setOnClickListener {
                handleAgroDealer()
            }

            cvFarmer.setOnClickListener {
                handleFarmer()
            }

            btnContinue.setOnClickListener {
                when (roleName) {
                    "Farmer" -> {
                        findNavController().navigate(
                            R.id.signInDetailsWithEmailFragment
                        )
                    }
                    "AgroDealer" -> {
                        findNavController().navigate(
                            R.id.agroDealerLoginFragment
                        )
                    }
                    else -> {
                        Timber.e("UserAccountTypeFragment"+" Role not found")
                        toast("Profile not set")
                        return@setOnClickListener
                    }
                }

            }
        }
    }

    private fun handleFarmer() {
        //activate select farmer acc type cardview
        roleName = "Farmer"

        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvFarmer.cardElevation = 4F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvFarmer.setTextColor(Color.WHITE)

        //deactivate acc type agrodealer
        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
        binding.cvAgrodealer.cardElevation = 2F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvAgrodealer.setTextColor(resources.getColor(R.color.main))
//        binding.tvAgrodealer.setTextColor(Color.parseColor("#ffe5b48d"));

    }

    private fun handleAgroDealer() {
        //activate select farmer acc type cardview
        roleName = "AgroDealer"

        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvAgrodealer.cardElevation = 4F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvAgrodealer.setTextColor(Color.WHITE)

        //deactivate acc type agrodealer
        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
        binding.cvFarmer.cardElevation = 2F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFarmer.setTextColor(resources.getColor(R.color.main))
//        binding.tvAgrodealer.setTextColor(Color.parseColor("#ffe5b48d"));
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}