package com.steve_md.smartmkulima.ui.fragments.merchant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.merchant.ui.fragment_adapter.FragmentAdapter
import com.ekenya.rnd.merchant.ui.fragments.deals.DealsCategoriesFragmentMerchant
import com.ekenya.rnd.merchant.ui.fragments.my_orders.MyOrdersFragmentMerchant
import com.steve_md.smartmkulima.databinding.FragmentHotDealsMerchantBinding


class HotDealsFragment : Fragment() {
    private lateinit var binding: FragmentHotDealsMerchantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentHotDealsMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setupViewPager()
    }

    private fun setupViewPager() {

        val fragmentsAdapter = FragmentAdapter(childFragmentManager).apply {
            addFragment(DealsCategoriesFragmentMerchant { navigateToDealsListFragment() }, "Deals")
            addFragment(MyOrdersFragmentMerchant(), "My Orders")
        }

        binding.apply {
            viewPager.adapter = fragmentsAdapter
            tabLayout.setupWithViewPager(viewPager)
        }

    }

    private fun navigateToDealsListFragment() {
      //  findNavController().navigate(R.id.action_hotDealsFragment_to_dealListFragment)
    }

}