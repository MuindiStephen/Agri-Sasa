package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.databinding.FragmentPredictYourCropProductionBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class PredictYourCropProductionFragment : Fragment() {

    private lateinit var binding: FragmentPredictYourCropProductionBinding
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

        binding.buttonGetGPSData.setOnClickListener {
            binding.inputTemperature.setText("25.8")
            binding.inputRainfall.setText("1200")
            binding.inputHumidity.setText("67.7")
        }

        binding.buttonGetIoTData.setOnClickListener {
            binding.inputSoilQualityPh.setText("5")
            binding.inputNBKLevel.setText("30.5")
        }

       binding.buttonPredict.setOnClickListener {
           val temperature = binding.inputTemperature.text.toString().toDouble().toString()
           val rainfall = binding.inputRainfall.text.toString().toDouble().toString()
           val soilQualityPh = binding.inputSoilQualityPh.text.toString()
           val selectedCrop = binding.spinnerCropPredict.selectedItem.toString()
           val selectedSeason = binding.spinnerSowingSeason.selectedItem.toString()
           val humidity = binding.inputHumidity.text.toString().toDouble().toString()


           if (temperature.isEmpty() && rainfall.isEmpty() && soilQualityPh.isEmpty()) {
               displaySnackBar("Required fields!")
               binding.enterTemperature.error = "Empty strings"
               binding.enterRainfall.error = "Empty strings"
               binding.enterSoilQualityPh.error = "Empty strings"
               Timber.tag(this.tag.toString()).v("Em")
           } else {

               val prediction = predictFarmProduce(
                   temperature.toDouble(),
                   rainfall.toDouble(),
                   soilQualityPh,
                   humidity.toDouble()
               )

               binding.textViewResult.text = "Expected production: $prediction tonnes"
               Timber.tag(this.tag.toString()).v("Prediction: $prediction tonnes")
           }
       }
    }

    // Dummy data for testing
    // Assumption
    private fun predictFarmProduce(
        temperature: Double,
        rainfall: Double,
        soilQualityPh: String,
        humidity: Double,
        ): Double {
        /**
         * Linear Regression Algorithm
         */
        val intercept = 10.0
        val tempCoeff = 2.0
        val rainCoeff = 1.5
        val soilQualityPhValue = soilQualityPh.toDoubleOrNull() ?: 7.0 //Default soil ph to be 7.0 if not provided
        val prediction = intercept + (humidity * tempCoeff) + (temperature * tempCoeff) + (rainfall * rainCoeff) + (soilQualityPhValue*0.5)

        return prediction
    }
}