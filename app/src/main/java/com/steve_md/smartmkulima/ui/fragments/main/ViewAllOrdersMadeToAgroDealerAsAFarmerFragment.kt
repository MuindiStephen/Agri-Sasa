package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgrodealerOrdersAdapter
import com.steve_md.smartmkulima.adapter.FarmersOrderToAgrodealersListAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewAllOrdersMadeToAgroDealerAsAFarmerBinding
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ViewAllOrdersMadeToAgroDealerAsAFarmerFragment : Fragment() {

    private lateinit var binding: FragmentViewAllOrdersMadeToAgroDealerAsAFarmerBinding
    private val viewModel: MainViewModel by viewModels()
    private val farmersOrderToAgrodealersListAdapter by lazy { FarmersOrderToAgrodealersListAdapter() }
    private var ordersList =  mutableListOf<OrderCheckoutByFarmer>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAllOrdersMadeToAgroDealerAsAFarmerBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        fetchMyPlacedOrdersAsAFarmer()
    }

    private fun fetchMyPlacedOrdersAsAFarmer() {
        lifecycleScope.launch {
            viewModel.allOrdersMadeToTheFarmer.observe(viewLifecycleOwner) { placedOrders->
                if (!placedOrders.isNullOrEmpty()) {
                    ordersList.clear()
                    ordersList.addAll(placedOrders)
                    farmersOrderToAgrodealersListAdapter.submitList(ordersList)
                    binding.noPlacedOrdersTV.isVisible = false

                    binding.orderAvailable.adapter = farmersOrderToAgrodealersListAdapter

                } else {
                    binding.noPlacedOrdersTV.isVisible = true
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.orderAvailable.layoutManager = LinearLayoutManager(requireContext())
        binding.orderAvailable.adapter = farmersOrderToAgrodealersListAdapter
    }

}