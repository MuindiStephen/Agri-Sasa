package com.steve_md.smartmkulima.ui.fragments.main.agrodealer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgrodealerOrdersOrderItemsListAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.FragmentAgroDealerViewOrderListItemsInDetailBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * View orders in details as an agrodealer user
 * logged in the app
 */
@AndroidEntryPoint
class AgroDealerViewOrderListItemsInDetailFragment : Fragment() {

    private lateinit var binding: FragmentAgroDealerViewOrderListItemsInDetailBinding
    private val args: AgroDealerViewOrderListItemsInDetailFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    private val orderDetailsAdapter by lazy { AgrodealerOrdersOrderItemsListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAgroDealerViewOrderListItemsInDetailBinding.inflate(
            inflater, container,false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar3.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val orderDetails = args.orderInDetailAgroDealer
        val agrodealerId = args.orderInDetailAgroDealer.agrodealerID

        orderDetails.let {
            orderDetailsAdapter.submitList(it.cartOrder)
        }

        binding.apply {
            recyclerViewOrderDetails.apply {
                layoutManager =  LinearLayoutManager(requireContext())
                adapter = orderDetailsAdapter
            }

            // Update order status with respect to the agrodealer ID
            btnChangeStatusOrders.setOnClickListener {
                viewModel.updateOrderStatus("Approved", agrodealerId )
                displaySnackBar("Order Approved.")
            }
        }

    }
}