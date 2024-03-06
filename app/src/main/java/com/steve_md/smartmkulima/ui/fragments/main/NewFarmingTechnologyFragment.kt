package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentNewFarmingTechnologyBinding
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
            }
            cardViewPredictFarmProduce.setOnClickListener {
                goToPredictFarmYield()
            }
            imageViewNewTechFarming.setOnClickListener {
                findNavController().navigateUp()
            }
            cardViewCropCycle.setOnClickListener {
                //goToGenerateNewCropCycle()
                goToAutoGenerateCropCycle()
            }
        }
    }

    private fun goToAutoGenerateCropCycle() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_autoCreateCropCycleFragment)
    }

    private fun goToGenerateNewCropCycle() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_cropCycleCreationAndScheduleFragment)
    }

    private fun goToPredictFarmYield() {
        findNavController().navigate(R.id.action_newFarmingTechnologyFragment_to_predictYourCropProductionFragment)
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