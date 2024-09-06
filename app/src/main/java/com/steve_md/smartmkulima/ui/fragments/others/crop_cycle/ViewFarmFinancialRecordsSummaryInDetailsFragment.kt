package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewFarmFinancialRecordsSummaryInDetailsBinding
import com.steve_md.smartmkulima.ui.activities.DetailedFarmCycleActivity
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import timber.log.Timber
import kotlin.math.exp

/**
 * ViewFarmFinancialRecordsSummary
 *
 * Analytics represented by a visual pie chart
 */
@AndroidEntryPoint
class ViewFarmFinancialRecordsSummaryInDetailsFragment : Fragment() {

    private lateinit var binding: FragmentViewFarmFinancialRecordsSummaryInDetailsBinding
    private var revenue: Double? = null

    private val args: ViewFarmFinancialRecordsSummaryInDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewFarmFinancialRecordsSummaryInDetailsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        setUpChart()
    }

    private fun setUpChart() {

        // Getting FarmFinancialDataSummary as an argument
        val farmRecord = args.farmFinancialRecordSummary

        val revenueFraction = revenue?.div((farmRecord.totalRevenueGenerated.toDouble() + farmRecord.totalInputCosts.toDouble() + revenue!!))
        val expensesFraction =  farmRecord.totalInputCosts.toDouble().div((farmRecord.totalInputCosts.toDouble() + farmRecord.totalRevenueGenerated.toDouble() + revenue!!))
        val salesFraction = farmRecord.totalRevenueGenerated.toDouble().div((farmRecord.totalInputCosts.toDouble() + farmRecord.totalRevenueGenerated.toDouble() + revenue!!))

        val pieChartSummaryFarmRecords = view?.findViewById<PieChart>(R.id.pieChartSummaryFarmRecords)
            pieChartSummaryFarmRecords?.slices = listOf(
                 PieChart.Slice(expensesFraction.toFloat(), Color.rgb(120, 181, 0), Color.rgb(149, 224, 0), legend = "Total Expenses/Costs"),
                 PieChart.Slice(salesFraction.toFloat(), Color.rgb(204, 168, 0), Color.rgb(249, 228, 0), legend = "Total Sales"),
                 PieChart.Slice(revenueFraction!!.toFloat(), Color.rgb(160, 100, 167), Color.rgb(165, 180, 185), legend = "Revenues Generated"),
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
        pieChartSummaryFarmRecords?.legendsTitle = "Crop Cycle Farm Records Summary"
        pieChartSummaryFarmRecords?.legendPosition = PieChart.LegendPosition.BOTTOM
        pieChartSummaryFarmRecords?.labelType = PieChart.LabelType.NONE
        pieChartSummaryFarmRecords?.isLegendEnabled = true
        pieChartSummaryFarmRecords?.legendsTitleColor = Color.BLACK
        pieChartSummaryFarmRecords?.legendsColor = Color.BLACK
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUi() {

        // Use of bundle
        /*
        binding.textView126.text = arguments?.getString("cropCycleName")
        binding.textView129.text = arguments?.getString("totalInputs")
        binding.textView132.text = arguments?.getString("totalSales")

         */

        // Use of Navigation Arguments
        val farmRecord = args.farmFinancialRecordSummary
        binding.textView126.text = "${farmRecord.nameOfCropCycle} crop cycle"
        binding.textView129.text = "Kes. ${farmRecord.totalRevenueGenerated}.00"
        binding.textView132.text = "Kes. ${farmRecord.totalInputCosts}.00"


        // val expenses = binding.textView129.text.toString().toDouble()
        // val sales = binding.textView132.text.toString().toDouble()

        revenue = farmRecord.totalRevenueGenerated.toDouble() - farmRecord.totalInputCosts.toDouble()

        // if sales > expenses
        if (farmRecord.totalRevenueGenerated.toDouble() > farmRecord.totalInputCosts.toDouble()) {
            binding.textView134.text = "+ Kes. $revenue"+"0"
        }
        // if expenses > sales
        else if (farmRecord.totalRevenueGenerated.toDouble() < farmRecord.totalInputCosts.toDouble()) {
            binding.textView134.text = "- Kes. $revenue"+"0"
        } else {
          return
        }

        binding.toolbarSummaryInDetailsBreakDown.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}