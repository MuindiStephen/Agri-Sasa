package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.shuhart.stepview.StepView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAutoCreateCropCycleBinding
import com.steve_md.smartmkulima.utils.toast
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AutoCreateCropCycleFragment : Fragment() {

    private lateinit var binding: FragmentAutoCreateCropCycleBinding
    private var cropCycleStartDay : Calendar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAutoCreateCropCycleBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        cropCycleStartDay = Calendar.getInstance()
        setUpBinding()
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    private fun setUpBinding() {
        binding.cropCycleStartDay.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        binding.buttonGeneratorCropCycle.setOnClickListener {

            val selectedCropType = binding.spinnerSelectCropType.selectedItem.toString()
            val startDayForCropCycle = binding.cropCycleStartDay.text.toString()
            val farmOrBlockId = binding.spinnerSelectFarmBlockID.selectedItem.toString()

            if (selectedCropType.isNotEmpty() && startDayForCropCycle.isNotEmpty() && farmOrBlockId.isNotEmpty()) {
                // Parse start date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDay = dateFormat.parse(startDayForCropCycle)

                // Sample crop cycle stages
                val stages = listOf(
                    "Seedling Acquisition",
                    "Media Acquisition",
                    "Media washing and Treatment",
                    "Preparation of grow trays (washing and sterilizing)/potting of media",
                    "Loading of treated media and seedlings",
                    "Seed sowing",
                    "Watering and fertilisation",
                    "Hardening",
                    "Land preparation",
                    "Manure application",
                    "Bed making/gypsum application",
                    "Flushing with plain water"
                )

                // Generate crop cycle schedule
//                val stateBuilder = binding.stepViewCropCycle.state
//                    .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.main))
//                    .animationType(StepView.ANIMATION_CIRCLE)
//                    .selectedCircleColor(ContextCompat.getColor(requireContext(), R.color.main))
//                    .selectedStepNumberColor(ContextCompat.getColor(requireContext(), R.color.bg_gray))
//                    .steps(stages)
//                    .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//                    .typeface(ResourcesCompat.getFont(requireContext(), R.font.nunito_sans_semibold))
//
//                stateBuilder.commit()

                // Display the result in linear layout
                binding.stepLinearLayout.removeAllViews()

                for (stage in stages) {
                    val stepTextView = TextView(requireContext())
                    val stageDescription = getStageDescription(stage)
                    stepTextView.text = "$stage\n$stageDescription"
                    stepTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.main1
                        )
                    )
                    stepTextView.textSize = 16f
                    stepTextView.setPadding(16, 16, 16, 16)

                    stepTextView.setOnClickListener {

                    }
                    binding.stepLinearLayout.addView(stepTextView)
                }

//
//                binding.tvResulT.setText(stages.map { stage ->
//                    val stageDescription = getStageDescription(stage)
//                    "$stage\n$stageDescription"
//                })

            }

        }
    }

    private fun showDatePickerDialog() {
        val calendar = cropCycleStartDay ?: Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selected date in the UI
                cropCycleStartDay?.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.cropCycleStartDay.setText(dateFormat.format(cropCycleStartDay!!.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getStageDescription(stageName: String): String {
        return when (stageName) {
            "Seedling Acquisition" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-10"
            "Media Acquisition" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-10"
            "Media washing and Treatment" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-11"
            "Preparation of grow trays (washing and sterilizing)/potting of media" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-12"
            "Loading of treated media and seedlings" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-14"
            "Seed sowing" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-15"
            "Watering and fertilisation" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Hardening" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Land preparation" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Manure application" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Bed making/gypsum application" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-23"
            "Flushing with plain water" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-24"
            else -> ""
        }
    }
}