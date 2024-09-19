package com.steve_md.smartmkulima.ui.fragments.main.agrodealer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgrodealerOrdersAdapter
import com.steve_md.smartmkulima.adapter.FarmExpensesRecordsAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.databinding.FragmentAgroDealerHomeDashboardBinding
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.ui.activities.DetailedFarmCycleActivity
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class AgroDealerHomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAgroDealerHomeDashboardBinding
    private lateinit var userProfileTxt: TextView
    private val viewModel: MainViewModel by viewModels()
    private lateinit var agrodealerOrdersAdapter : AgrodealerOrdersAdapter
    private var ordersList =  mutableListOf<OrderCheckoutByFarmer>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAgroDealerHomeDashboardBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfileTxt = view.findViewById<TextView>(R.id.userNameTextView)

        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }

        // fetch Orders for Respective Agro-Dealer ID...
        arguments?.getString("agrodealerID")

        setUpOrdersRecyclerView()

        setUpUi()

        lifecycleScope.launch {
            arguments?.getString("agrodealerID")?.let {
                viewModel.ordersByAgroDealerID(it).observe(viewLifecycleOwner) { orders->
                    if (!orders.isNullOrEmpty()) {
                        ordersList.clear()
                        ordersList.addAll(orders)
                        agrodealerOrdersAdapter.submitList(ordersList)
                        binding.noOrders.isVisible = false

                        binding.recyclerView5.adapter = agrodealerOrdersAdapter

                    } else {
                        binding.noOrders.isVisible = true
                    }
                }
            }
        }
    }


    private fun setUpUi() {
        when(view?.findViewById<TextView>(R.id.textView159)?.text.toString() == "Status: Approved") {
            true -> {
                view?.findViewById<TextView>(R.id.textView159)
                    ?.setTextColor(resources.getColor(R.color.light_blue))
            }
            false -> {
                view?.findViewById<TextView>(R.id.textView159)
                    ?.setTextColor(resources.getColor(R.color.fireBrickRed))
            }
        }
    }

    private fun setUpOrdersRecyclerView() {
        // Set the layout manager
        binding.recyclerView5.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        agrodealerOrdersAdapter = AgrodealerOrdersAdapter(AgrodealerOrdersAdapter.OnClickListener{ order ->
            Timber.tag("...CreatedFarmCycles....").e(order.toString())

            Timber.i("=====Checking==ORDER WITH ID=====>: ${order.orderId} ")

            val directions = AgroDealerHomeDashboardFragmentDirections.actionAgroDealerHomeDashboardFragmentToAgroDealerViewOrderListItemsInDetailFragment(
                orderInDetailAgroDealer = order
            )
            findNavController().navigate(directions)

        })

        // Set the adapter to the RecyclerView
        binding.recyclerView5.adapter = agrodealerOrdersAdapter
    }
}