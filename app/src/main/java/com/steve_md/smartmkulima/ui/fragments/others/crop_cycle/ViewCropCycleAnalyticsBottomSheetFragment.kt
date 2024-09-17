package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentViewCropCycleAnalyticsBottomSheetBinding
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import timber.log.Timber
import kotlin.math.exp

@AndroidEntryPoint
class ViewCropCycleAnalyticsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentViewCropCycleAnalyticsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentViewCropCycleAnalyticsBottomSheetBinding.inflate(
            inflater,container,false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onCreate - Fragment: ${arguments?.getString("cropCycleName")}")

        val cropName = arguments?.getString("cropCycleName")

        if (cropName != null) {
            viewModel.setSelectedCrop(cropName)
        }

        viewModel.selectedCrop.observe(viewLifecycleOwner) { cropName ->
            cropName?.let {
               Timber.d("Found records for $cropName")
            } ?: run {
               Timber.d("No records, no crop found in the records")

            }
        }

        // Observe total expenses and set it in the expenses TextInputLayout
        viewModel.totalExpenseForCrop.observe(viewLifecycleOwner) { expenses ->
            expenses?.let {
                binding.textView151.setText(it)
            }
        }

        // sales made observers
        viewModel.totalSalesForCrop.observe(viewLifecycleOwner) { sales ->
            sales?.let {
                binding.textView152.setText(it)
            }
        }

        // Observe calculated revenue
        viewModel.calculatedRevenue.observe(viewLifecycleOwner) { revenue ->
            revenue?.let {
                binding.textView153.setText(it)
            }
        }

        setUpLogic()

        setUiChart()
    }

    private fun setUiChart() {

        val revenueString = binding.textView153.text.toString()
        val expensesString = binding.textView151.text.toString()
        val salesString = binding.textView152.text.toString()

        val revenue = revenueString.toDoubleOrNull() ?: 200.0
        val expenses = expensesString.toDoubleOrNull() ?: 800.0
        val sales = salesString.toDoubleOrNull() ?: 1000.0

        val total = sales + expenses + revenue

        if (total > 0) {
            val revenueFraction = revenue / total
            val expensesFraction = expenses / total
            val salesFraction = sales / total

            val pieChartSummaryFarmRecords = view?.findViewById<PieChart>(R.id.pieChartSummaryFarmRecords)
            pieChartSummaryFarmRecords?.slices = listOf(
                PieChart.Slice(expensesFraction.toFloat(), Color.rgb(120, 181, 0), Color.rgb(149, 224, 0), legend = "Total Expenses/Costs"),
                PieChart.Slice(salesFraction.toFloat(), Color.rgb(204, 168, 0), Color.rgb(249, 228, 0), legend = "Total Sales"),
                PieChart.Slice(revenueFraction.toFloat(), Color.rgb(160, 100, 167), Color.rgb(165, 180, 185), legend = "Revenues Generated")
            )

            pieChartSummaryFarmRecords?.apply {
                gradientType = PieChart.GradientType.RADIAL
                legendIconsMargin = Dimension.DP(8F)
                legendTitleMargin = Dimension.DP(14F)
                legendLinesMargin = Dimension.DP(10F)
                legendsMargin = Dimension.DP(20F)
                legendsPercentageMargin = Dimension.DP(8F)
                legendsSize = Dimension.DP(11F)
                legendsPercentageSize = Dimension.DP(11F)
                legendsIcon = PieChart.DefaultIcons.SQUARE
                legendsTitle = "Analytics"
                legendPosition = PieChart.LegendPosition.BOTTOM
                labelType = PieChart.LabelType.NONE
                isLegendEnabled = true
                legendsTitleColor = Color.BLACK
                legendsColor = Color.BLACK
            }
        } else {
            Timber.e("Cannot create chart because total value is zero or invalid.")
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setUpLogic() {
        val expensesString = binding.textView151.text.toString()
        val salesString = binding.textView152.text.toString()

        // Use safe conversion to handle invalid numbers
        val expenses = expensesString.toDoubleOrNull() ?: 0.0
        val sales = salesString.toDoubleOrNull() ?: 0.0

        // Update textView134 based on the comparison
        when {
            sales > expenses -> {
                binding.textView134.text = "+ Kes."
            }
            sales < expenses -> {
                binding.textView134.text = "- Kes."
            }
            else -> {
                binding.textView134.text = "Kes."
            }
        }
    }

    companion object {
        const val TAG = "ViewCropCycleAnalyticsBottomSheetFragment"
    }

}