package com.ekenya.rnd.merchant.ui.fragments.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.merchant.databinding.FragmentDealsCategoriesMerchantBinding


class DealsCategoriesFragmentMerchant(private val navigateToDealsList: () -> Unit) : Fragment() {

    private lateinit var binding: FragmentDealsCategoriesMerchantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentDealsCategoriesMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val categoryAdapter = DealCategoryAdapter { onItemClicked() }
            categoryAdapter.submitList(mutableListOf<DealCategory>().apply {
                add(
                    DealCategory(
                        "Ramadan Mega Sale",
                        "Get up to 50% off for every purchase orders made on the products available on this sale"
                    )
                )
                add(
                    DealCategory(
                        "Black Friday",
                        "Get up to 50% off for every purchase orders made on the products available on this sale"
                    )
                )
                add(
                    DealCategory(
                        "Ramadan Mega Sale",
                        "Get up to 50% off for every purchase orders made on the products available on this sale"
                    )
                )
                add(
                    DealCategory(
                        "Black Friday",
                        "Get up to 50% off for every purchase orders made on the products available on this sale"
                    )
                )
            })
            adapter = categoryAdapter
        }
    }

    private fun onItemClicked() {
        navigateToDealsList()
    }

}