package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    private fun setUpBinding() {
        binding.cropCycleStartDay.setOnClickListener { showDatePickerDialog() }

        binding.buttonGeneratorCropCycle.setOnClickListener {

            val selectedCropType = binding.spinnerSelectCropType.selectedItem.toString()
            val startDayForCropCycle = binding.cropCycleStartDay.text.toString()
            val farmOrBlockId = binding.spinnerSelectFarmBlockID.selectedItem.toString()

            if (startDayForCropCycle.isNotEmpty()) {

                //val startDayString = "${startDayForCropCycle[Calendar.YEAR]}-${startDayForCropCycle[Calendar.MONTH] + 1}-${startDayForCropCycle[Calendar.DAY_OF_MONTH]}"

                val cropCycle = generateNewCropCycleForThisCrop(selectedCropType, startDayForCropCycle, farmOrBlockId)

                insertCropCycleToFirebase(selectedCropType, cropCycle)

                displayCropCycleSteps(cropCycle)
            } else {
                toast("Start Day is empty! Click to select.")
            }
        }
    }

    private fun displayCropCycleSteps(cropCycle: List<String>) {

        binding.scrollCropCycle.visibility = View.VISIBLE

        val steps = StringBuilder()
        for (i in cropCycle.indices) {
            steps.append("${i + 1}. ${cropCycle[i]}\n")
        }
        binding.displayCropCycle.text = steps.toString()
    }

    private fun insertCropCycleToFirebase(selectedCropType: String, cropCycle: List<String>) {

    }
    private fun generateNewCropCycleForThisCrop(selectedCropType: String, startDayCropCycle: String, farmOrBlockId: String): List<String> {
        val taskNames = resources.getStringArray(R.array.task_names)
        val taskDurations = resources.getIntArray(R.array.task_durations)
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val calendar = Calendar.getInstance()

        val cropCycle = mutableListOf<String>()
        calendar.time = format.parse(startDayCropCycle) ?: Date()


        var startDay = 1
        for (i in taskNames.indices) {
            val endDay = startDay + taskDurations[i]
            cropCycle.add("${taskNames[i]}: Start Day $startDay - End Day $endDay")
            startDay = endDay + 1
        }
        Timber.v("$cropCycle generated for $selectedCropType at Farm $farmOrBlockId")
        return cropCycle
    }

    private fun showDatePickerDialog() {
        val year = cropCycleStartDay?.get(Calendar.YEAR)
        val month = cropCycleStartDay?.get(Calendar.MONTH)
        val day = cropCycleStartDay?.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                cropCycleStartDay?.set(Calendar.YEAR, year)
                cropCycleStartDay?.set(Calendar.MONTH, monthOfYear)
                cropCycleStartDay?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateStartDay()

            }, year!!, month!!, day!!)

        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateStartDay() {
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val formattedStartDay = sdf.format(cropCycleStartDay?.time ?: Date())
        binding.cropCycleStartDay.text = "Start Day: $formattedStartDay"
    }
}