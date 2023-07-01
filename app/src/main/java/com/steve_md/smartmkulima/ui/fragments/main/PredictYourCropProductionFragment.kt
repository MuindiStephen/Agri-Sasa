package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.databinding.FragmentPredictYourCropProductionBinding


class PredictYourCropProductionFragment : Fragment() {

    private lateinit var binding:FragmentPredictYourCropProductionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPredictYourCropProductionBinding.inflate(inflater,container,false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.buttonPredict.setOnClickListener {
           val temperature = binding.inputTemperature.text.toString().toDouble()
           val rainfall = binding.inputRainfall.text.toString().toDouble()

           // call prediction function here :)
           val prediction = predictFarmProduce(temperature, rainfall)

           binding.textViewResult.text = "Expected production: $prediction"

       }
    }

    private fun predictFarmProduce(temperature: Double, rainfall: Double): Double {
        /**
         * Linear Regression Algorithm
         */
        val intercept = 10.0
        val tempCoeff = 2.0
        val rainCoeff = 1.5

        val prediction = intercept + (temperature * tempCoeff) + (rainfall * rainCoeff)

        return prediction
    }
}