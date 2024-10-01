package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentCropCycleAddNewExpensesBinding
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.utils.DateFormat
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class CropCycleAddNewExpensesFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleAddNewExpensesBinding
    private val viewModel: MainViewModel by viewModels()
    private var expenseDay: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleAddNewExpensesBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        expenseDay = Calendar.getInstance()

        fetchFirstAvailableCropCycles()
        setUpVBinding()
        setUpDatePickerUi()
    }

    private fun setUpDatePickerUi() {
        binding.inputExpenseDate.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = expenseDay ?: Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selected date in the UI
                expenseDay?.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.inputExpenseDate.setText(DateFormat.getWhenStarts())
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun fetchFirstAvailableCropCycles() {

        lifecycleScope.launch {
            viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
                if (cycles.isNotEmpty()) {

                    val allCropCycles = cycles.map {
                        "${it.cropName} - ${it.farmName} - ${it.startDate}"
                    }

                    val adapter = ArrayAdapter<String>(this@CropCycleAddNewExpensesFragment.requireContext(),
                        android.R.layout.simple_spinner_item, allCropCycles)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerNameOfCycle.adapter = adapter
                    Timber.d("Crop cycles are available and in progress...Update any records")
                } else {
                    displaySnackBar("No crop cycle found, you cannot create any records.")
                }
            }
        }
    }

    private fun setUpVBinding() {

        binding.toolbar3Expense.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCreateCropCycleExpense.setOnClickListener {
            if (validateInputs()) {

                val farmFinanceExpenseRecords = FarmFinanceExpenseRecords(
                    nameOfCropCycle = binding.spinnerNameOfCycle.selectedItem.toString() ,
                    nameOfExpense = binding.inputExpenseName.text.toString(),
                    amountSpent = binding.inputExpenseAmount.text.toString() ,
                    whichTask = binding.spinnerentertaskname.selectedItem.toString(),
                    dateOfThisFinancialRecord = binding.inputExpenseDate.text.toString()
                )

                lifecycleScope.launch {
                    try {
                        viewModel.addFarmCycleExpense(farmFinanceExpenseRecords)
                        Timber.d("Request succeeded.")
                        requireActivity().runOnUiThread {
                            displaySnackBar("Successfully Added a new expense record.")
                        }
                        findNavController().navigate(R.id.cropCycleFarmRecordsFragment)
                    } catch (e: Exception) {
                        displaySnackBar("Your request failed.")
                        Timber.d("Request failed.")
                        Timber.tag("Add==Expenses==").e(
                            "Request==Failed ==>Could not create this expense record ${e.localizedMessage}"
                        )

                        findNavController().navigate(R.id.cropCycleFarmRecordsFragment)
                    }
                }

            }
        }
    }

    /**
     * Validating the input fields - ui
     */
    private fun validateInputs(): Boolean {
        return binding.inputExpenseName.text.isNullOrEmpty().not().also {
            if (!it) binding.enterExpenseName.error = "Invalid"
        } && binding.inputExpenseAmount.text.isNullOrEmpty().not().also {
            if (!it) binding.enterAmountSpent.error = "Invalid"
        } && binding.inputTaskName.text.isNullOrEmpty().not().also {
            if (!it) binding.enterTaskName.error = "Invalid"
        } && binding.inputExpenseDate.text.isNullOrEmpty().not().also {
            if (!it) binding.enterDate.error = "Invalid"
        }
    }
}