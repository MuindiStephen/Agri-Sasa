package com.steve_md.smartmkulima.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentNewFarmingTechnologyBinding
import com.steve_md.smartmkulima.ui.activities.FarmManagementActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewFarmingTechnologyFragment : Fragment() {

    private lateinit var binding: FragmentNewFarmingTechnologyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewFarmingTechnologyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpBinding()
    }

    private fun setUpBinding() {
        binding.apply {
            cardViewGPS.setOnClickListener {
                navigateToGPSTrackFarmConditions()
            }
            cardViewReachMarket.setOnClickListener {
                navigateToMarketProduce()
            }
            cardViewTrackAgriTechCompany.setOnClickListener {
                 goToLocateAgriTechs()

//                val intent = Intent(requireActivity(), MapsActivity::class.java)
//                startActivity(intent)
//                intent.flags = 0
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                requireActivity().finish()

            }
            cardViewPredictFarmProduce.setOnClickListener {
                goToPredictFarmYield()
            }
            imageViewNewTechFarming.setOnClickListener {
                findNavController().navigateUp()
            }
            cardViewCropCycle.setOnClickListener {
                //goToGenerateNewCropCycle()
                // goToAutoGenerateCropCycle()

                navigateToFarmMngtActivity()
            }
        }
    }

    private fun navigateToFarmMngtActivity() {
        startActivity(Intent(requireContext(), FarmManagementActivity::class.java))
        requireActivity().finish()
    }

    private fun goToAutoGenerateCropCycle() {
        findNavController().navigate(
            R.id.action_newFarmingTechnologyFragment_to_autoCreateCropCycleFragment
        )
    }

    private fun goToGenerateNewCropCycle() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_cropCycleCreationAndScheduleFragment)
    }

    private fun goToPredictFarmYield() {
        findNavController().navigate(
            R.id.action_newFarmingTechnologyFragment_to_predictYourCropProductionFragment
        )
    }

    private fun goToLocateAgriTechs() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_locateAgriTechCompaniesFragment)
    }

    private fun navigateToMarketProduce() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_marketProduce)
    }

    private fun navigateToGPSTrackFarmConditions() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_monitorFarmConditionFragment)
    }
}