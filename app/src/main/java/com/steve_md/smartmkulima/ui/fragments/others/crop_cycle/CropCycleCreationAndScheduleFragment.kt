package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentCropCycleCreationAndScheduleBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CropCycleCreationAndScheduleFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleCreationAndScheduleBinding
    private val startDate =  Calendar.getInstance()
    private val endDate = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCropCycleCreationAndScheduleBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.textViewTaskStartDate.setOnClickListener { showDatePickerDialog(true) }
        binding.textViewTaskEndDate.setOnClickListener { showDatePickerDialog(false) }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = if (isStartDate) startDate else endDate

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                showTimePicker(isStartDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker(isStartDate: Boolean) {

        val calendar = if (isStartDate) startDate else endDate

        val timePickerDialog = TimePickerDialog(requireContext().applicationContext,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(calendar.time)

                if (isStartDate) {
                    binding.textViewTaskStartDate.text = "Start Date: $formattedDateTime"
                } else {
                    binding.textViewTaskEndDate.text = "End Date: $formattedDateTime"
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

        timePickerDialog.show()
    }

}