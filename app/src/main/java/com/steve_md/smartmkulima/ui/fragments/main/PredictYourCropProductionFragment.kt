package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.databinding.FragmentPredictYourCropProductionBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
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

        binding.imageView13.setOnClickListener {
            findNavController().navigateUp()
        }

       binding.buttonPredict.setOnClickListener {
           val temperature = binding.inputTemperature.text.toString().toDouble()
           val rainfall = binding.inputRainfall.text.toString().toDouble()
           val soilQualityPh = binding.inputSoilQualityPh.text.toString()
           val selectedCrop = binding.spinnerCropPredict.selectedItem.toString()
           val selectedSeason = binding.spinnerSowingSeason.selectedItem.toString()

           // call prediction function here :)
           if (temperature != null && rainfall != null &&
               soilQualityPh != null && selectedCrop!= null
               && selectedSeason!=null){
               val prediction = predictFarmProduce(
                   temperature,
                   rainfall,
                   soilQualityPh,
                   selectedSeason,
                   selectedCrop
               )
               binding.textViewResult.text = "Expected production: $prediction"

               Timber.tag(this.tag.toString()).v("Prediction: $prediction")
           }
           else {
               binding.enterTemperature.error = "Empty strings"
               binding.enterRainfall.error = "Empty strings"
               binding.enterSoilQualityPh.error = "Empty strings"
           }
       }
    }
    private fun predictFarmProduce(
        temperature: Double,
        rainfall: Double,
        soilQualityPh: String,
        selectedSeason: String,
        selectedCrop: String,
    ): Double {
        /**
         * Linear Regression Algorithm
         */
        val intercept = 10.0
        val tempCoeff = 2.0
        val rainCoeff = 1.5
        val soilQualityPhValue = soilQualityPh.toDoubleOrNull() ?: 7.0 //Default soil ph to be 7.0 if not provided
        val prediction = intercept + (temperature * tempCoeff) + (rainfall * rainCoeff) + (soilQualityPhValue*0.5)

        return prediction
    }
}