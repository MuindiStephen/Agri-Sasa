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
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewFarmFinancialRecordsSummaryInDetailsBinding
import com.steve_md.smartmkulima.ui.activities.DetailedFarmCycleActivity
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import timber.log.Timber

@AndroidEntryPoint
class ViewFarmFinancialRecordsSummaryInDetailsFragment : Fragment() {

    private lateinit var binding: FragmentViewFarmFinancialRecordsSummaryInDetailsBinding
    private var revenue: Double? = null
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

        val revenueFraction = revenue?.div((revenue!! + binding.textView129.text.toString().toDouble()))
        val expensesFraction =  binding.textView129.text.toString().toDouble().div((revenue!! + binding.textView129.text.toString().toDouble()))

        val pieChartSummaryFarmRecords = view?.findViewById<PieChart>(R.id.pieChartSummaryFarmRecords)
        pieChartSummaryFarmRecords?.slices = listOf(
              PieChart.Slice(revenueFraction!!.toFloat(), Color.rgb(120, 181, 0), Color.rgb(149, 224, 0), legend = "Total Expenses/Costs"),
              PieChart.Slice(expensesFraction.toFloat(), Color.rgb(204, 168, 0), Color.rgb(249, 228, 0), legend = "Total Revenues"),
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

        binding.textView126.text = arguments?.getString("cropCycleName")
        binding.textView129.text = arguments?.getString("totalInputs")
        binding.textView132.text = arguments?.getString("totalSales")


        val expenses = binding.textView129.text.toString().toDouble()
        val sales = binding.textView132.text.toString().toDouble()

         revenue = binding.textView132.text.toString().toDouble() - binding.textView129.text.toString().toDouble()

                // if sales > expenses
        if (binding.textView132.text.toString().toDouble() > binding.textView129.text.toString().toDouble()) {
            binding.textView134.text = "+ Kes $revenue"
        }
        // if expenses > sales
        else if (binding.textView132.text.toString().toDouble() < binding.textView129.text.toString().toDouble()) {
            binding.textView134.text = "- Kes $revenue"
        } else {
          return
        }

        binding.toolbarSummaryInDetailsBreakDown.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}