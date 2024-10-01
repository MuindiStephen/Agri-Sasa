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

            cvBuyers.setOnClickListener {
                handleBuyers()
            }

            cvFieldagents.setOnClickListener {
                handleFieldAgents()
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

                    "FieldAgent" -> {
                       findNavController().navigate(R.id.fieldAgentRegisterFragment)
                    }

                    "Buyer" -> {
                        findNavController().navigate(R.id.marketProduce)
                    }
                    else -> {
                        Timber.e("UserAccountTypeFragment"+"Role not found")
                        toast("Account type not set")
                        return@setOnClickListener
                    }
                }

            }
        }
    }

    private fun handleFieldAgents() {
        roleName = "FieldAgent"

        binding.cvFieldagents.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvFieldagents.cardElevation = 4F
        binding.ivFieldagent.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvFieldagent.setTextColor(Color.WHITE)

        //deactivate acc type agrodealer
        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvAgrodealer.cardElevation = 2F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvAgrodealer.setTextColor(resources.getColor(R.color.main))

        // Deactivate Farmer
        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFarmer.cardElevation = 2F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFarmer.setTextColor(resources.getColor(R.color.main))

        // Deactivate buyer
        binding.cvBuyers.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvBuyers.cardElevation = 2F
        binding.ivBuyer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvBuyer.setTextColor(resources.getColor(R.color.main))
    }

    private fun handleBuyers() {
        roleName = "Buyer"

        binding.cvBuyers.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvBuyers.cardElevation = 4F
        binding.ivBuyer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvBuyer.setTextColor(Color.WHITE)

        // Deactivate acc type agro-dealer
        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvAgrodealer.cardElevation = 2F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvAgrodealer.setTextColor(resources.getColor(R.color.main))

        // Deactivate Farmer
        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFarmer.cardElevation = 2F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFarmer.setTextColor(resources.getColor(R.color.main))

        // Deactivate Field-agent
        binding.cvFieldagents.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFieldagents.cardElevation = 2F
        binding.ivFieldagent.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFieldagent.setTextColor(resources.getColor(R.color.main))

    }

    private fun handleFarmer() {
        //activate select farmer acc type cardview
        roleName = "Farmer"

        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvFarmer.cardElevation = 4F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvFarmer.setTextColor(Color.WHITE)

        //deactivate acc type agrodealer
        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvAgrodealer.cardElevation = 2F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvAgrodealer.setTextColor(resources.getColor(R.color.main))
//        binding.tvAgrodealer.setTextColor(Color.parseColor("#ffe5b48d"));

        // Deactivate Field-agent
        binding.cvFieldagents.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFieldagents.cardElevation = 2F
        binding.ivFieldagent.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFieldagent.setTextColor(resources.getColor(R.color.main))

        // Deactivate buyer
        binding.cvBuyers.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvBuyers.cardElevation = 2F
        binding.ivBuyer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvBuyer.setTextColor(resources.getColor(R.color.main))

    }

    private fun handleAgroDealer() {
        //activate select agrodealer acc type cardview
        roleName = "AgroDealer"

        binding.cvAgrodealer.setCardBackgroundColor(resources.getColor(R.color.main))
        binding.cvAgrodealer.cardElevation = 4F
        binding.ivAgrodealer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.bg_gray))
        binding.tvAgrodealer.setTextColor(Color.WHITE)

        //deactivate acc type farmer
        binding.cvFarmer.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFarmer.cardElevation = 2F
        binding.ivFarmer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFarmer.setTextColor(resources.getColor(R.color.main))
//        binding.tvAgrodealer.setTextColor(Color.parseColor("#ffe5b48d"));

        // Deactivate Field-agent
        binding.cvFieldagents.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvFieldagents.cardElevation = 2F
        binding.ivFieldagent.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvFieldagent.setTextColor(resources.getColor(R.color.main))

        // Deactivate buyer
        binding.cvBuyers.setCardBackgroundColor(resources.getColor(R.color.very_light_gray))
        binding.cvBuyers.cardElevation = 2F
        binding.ivBuyer.setColorFilter(ContextCompat.getColor(requireContext(),R.color.main))
        binding.tvBuyer.setTextColor(resources.getColor(R.color.main))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


     // on resume to this screen just set role to farmer by default
    override fun onResume() {
        super.onResume()
        roleName = "Farmer"
        handleFarmer()
    }
}