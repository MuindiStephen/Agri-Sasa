package com.steve_md.smartmkulima.ui.fragments.main.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.fragmentadapter.FragmentAdapter
import com.steve_md.smartmkulima.databinding.FragmentMyCartBuyerBinding
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.CropCycleExpensesListFragment
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.CropCycleRevenuesListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCartBuyerFragment : Fragment() {

    private lateinit var binding: FragmentMyCartBuyerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCartBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        val fragmentsAdapter = FragmentAdapter(childFragmentManager).apply {
            addFragment(CurrentOrderBuyerFragment(),"Current Order")
            addFragment(OrderHistoryBuyerFragment(), "Orders History")
        }

        binding.apply {
            viewPager.adapter = fragmentsAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }
}