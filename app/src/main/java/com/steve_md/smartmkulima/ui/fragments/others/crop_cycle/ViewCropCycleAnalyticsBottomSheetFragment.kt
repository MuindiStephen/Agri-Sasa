package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentViewCropCycleAnalyticsBottomSheetBinding
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import timber.log.Timber


class ViewCropCycleAnalyticsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: MainViewModel by viewModels()
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

        arguments?.getString("cropCycleName")?.let { viewModel.setSelectedCrop(it) }


        viewModel.selectedCrop.observe(viewLifecycleOwner) { cropName ->
            cropName?.let {
               Timber.d("Found records for $cropName")
            } ?: run {
               Timber.d("No records for $cropName")
            }
        }

        // Observe total expenses and set it in the expenses TextInputLayout
        viewModel.totalExpenseForCrop.observe(viewLifecycleOwner) { expenses ->
            expenses?.let {
                binding.textView132.text = it.toString()
            }
        }

        viewModel.totalSalesForCrop.observe(viewLifecycleOwner) { sales ->
            sales?.let {
                binding.textView129.text = it.toString()
            }
        }

        // Observe calculated revenue
        viewModel.calculatedRevenue.observe(viewLifecycleOwner) { revenue ->
            binding.textView134.text = revenue?.toString() ?: "0.0"
        }

        setUiChart()

        setUpLogic()
    }

    private fun setUpLogic() {
        if (binding.textView129.text.toString().toDouble() > binding.textView132.text.toString().toDouble()) {
            binding.textView134.text = "+ Kes. ${binding.textView134.text.toString().toDouble()}"+"0"
        }
        // if expenses > sales
        else if (binding.textView129.text.toString().toDouble() < binding.textView132.text.toString().toDouble()) {
            binding.textView134.text = "- Kes. ${binding.textView134.text.toString().toDouble()}"+"0"
        } else if (binding.textView129.text.toString().toDouble() == binding.textView132.text.toString().toDouble()) {
            binding.textView134.text = "Kes. 0.0"
        }
        else{
            return
        }
    }

    private fun setUiChart() {


        val revenueFraction = binding.textView134.text.toString().toDouble().div((binding.textView129.text.toString().toDouble() + binding.textView132.text.toString().toDouble() + binding.textView134.text.toString().toDouble()))
        val expensesFraction = binding.textView132.text.toString().toDouble().div((binding.textView132.text.toString().toDouble() + binding.textView129.text.toString().toDouble() + binding.textView134.text.toString().toDouble()))
        val salesFraction = binding.textView129.text.toString().toDouble().div((binding.textView132.text.toString().toDouble() + binding.textView129.text.toString().toDouble() + binding.textView134.text.toString().toDouble()))

        val pieChartSummaryFarmRecords = view?.findViewById<PieChart>(R.id.pieChartSummaryFarmRecords)
        pieChartSummaryFarmRecords?.slices = listOf(
            PieChart.Slice(expensesFraction.toFloat(), Color.rgb(120, 181, 0), Color.rgb(149, 224, 0), legend = "Total Expenses/Costs"),
            PieChart.Slice(salesFraction.toFloat(), Color.rgb(204, 168, 0), Color.rgb(249, 228, 0), legend = "Total Sales"),
            PieChart.Slice(revenueFraction.toFloat(), Color.rgb(160, 100, 167), Color.rgb(165, 180, 185), legend = "Revenues Generated"),
        )

        pieChartSummaryFarmRecords?.gradientType = PieChart.GradientType.RADIAL
        pieChartSummaryFarmRecords?.legendIconsMargin = Dimension.DP(8F)
        pieChartSummaryFarmRecords?.legendTitleMargin = Dimension.DP(14F)
        pieChartSummaryFarmRecords?.legendLinesMargin = Dimension.DP(10F)
        pieChartSummaryFarmRecords?.legendsMargin = Dimension.DP(20F)
        pieChartSummaryFarmRecords?.legendsPercentageMargin = Dimension.DP(8F)
        pieChartSummaryFarmRecords?.legendsSize = Dimension.DP(11F)
        pieChartSummaryFarmRecords?.legendsPercentageSize = Dimension.DP(11F)
        pieChartSummaryFarmRecords?.legendsIcon = PieChart.DefaultIcons.SQUARE
        pieChartSummaryFarmRecords?.legendsTitle = "Crop Cycle Analytics"
        pieChartSummaryFarmRecords?.legendPosition = PieChart.LegendPosition.BOTTOM
        pieChartSummaryFarmRecords?.labelType = PieChart.LabelType.NONE
        pieChartSummaryFarmRecords?.isLegendEnabled = true
        pieChartSummaryFarmRecords?.legendsTitleColor = Color.BLACK
        pieChartSummaryFarmRecords?.legendsColor = Color.BLACK
    }

    companion object {
        const val TAG = "ViewCropCycleAnalyticsBottomSheetFragment"
    }

}