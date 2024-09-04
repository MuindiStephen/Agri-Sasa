package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.content.Intent
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
import timber.log.Timber

@AndroidEntryPoint
class ViewFarmFinancialRecordsSummaryInDetailsFragment : Fragment() {

    private lateinit var binding: FragmentViewFarmFinancialRecordsSummaryInDetailsBinding
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
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUi() {

        binding.textView126.text = arguments?.getString("cropCycleName")

        binding.textView129.text = arguments?.getString("totalInputs")
        binding.textView132.text = arguments?.getString("totalSales")


        val expenses = binding.textView129.text.toString().toDouble()
        val sales = binding.textView132.text.toString().toDouble()

        val revenue = binding.textView132.text.toString().toDouble() - binding.textView129.text.toString().toDouble()

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