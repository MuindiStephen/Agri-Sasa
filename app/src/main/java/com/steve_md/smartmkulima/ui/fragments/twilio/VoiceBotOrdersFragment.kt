package com.steve_md.smartmkulima.ui.fragments.twilio

import android.content.Intent
import android.os.Bundle
import android.speech.tts.Voice
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.OrderItemsVoiceBotAdapter
import com.steve_md.smartmkulima.adapter.VoiceBotOrdersAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.databinding.FragmentVoiceBotOrdersBinding
import com.steve_md.smartmkulima.model.voicebotresponse.Order
import com.steve_md.smartmkulima.ui.activities.DetailedFarmCycleActivity
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VoiceBotOrdersFragment : Fragment() {

    private lateinit var binding: FragmentVoiceBotOrdersBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var voiceBotOrdersAdapter: VoiceBotOrdersAdapter

    private val orderItemsAdapter by lazy { OrderItemsVoiceBotAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentVoiceBotOrdersBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpRecyclerView()

        getAllVoiceBotOrders()
    }


    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.rVBotOrders.layoutManager = LinearLayoutManager(requireContext())



        // Initialize the adapter
        voiceBotOrdersAdapter = VoiceBotOrdersAdapter(
            VoiceBotOrdersAdapter.OnClickListener { order->
            Timber.tag("...CreatedFarmCycles....").e(order.toString())

            Timber.i("=====Checking=======>: ${order.orderId} cycle")

            val direction = VoiceBotOrdersFragmentDirections.actionVoiceBotOrdersFragmentToVoiceOrderInDetailFragment(
                orderitem = order
            )

            findNavController().navigate(
                direction
            )

//            val bundle = Bundle().apply {
//                putParcelable("orderitem", order)
//            }
//            findNavController().navigate(R.id.action_voiceBotOrdersFragment_to_voiceOrderInDetailFragment, bundle)

        })

        // Set the adapter to the RecyclerView
        binding.rVBotOrders.adapter = voiceBotOrdersAdapter
    }

    private fun getAllVoiceBotOrders() {
        viewModel.voiceBotOrders.observe(viewLifecycleOwner) { orders ->
            if (orders.data.isEmpty()) {
                displaySnackBar("No orders available")
            } else {

                voiceBotOrdersAdapter.submitList(orders.data.toList())
                binding.rVBotOrders.adapter = voiceBotOrdersAdapter

                binding.apply {


                    // orderItemsAdapter.submitList(orderItem.items)

                    val rVOrderInDetail = view?.findViewById<RecyclerView>(R.id.rVOrderInDetail)

                    val allItems = orders.data.flatMap { it.items }

                    rVOrderInDetail?.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = orderItemsAdapter
                        orderItemsAdapter.submitList(allItems) // Ensure data is submitted after adapter is set
                    }

                    orderItemsAdapter.submitList(allItems)

//                    orders.data.firstOrNull()?.items.let {
//
//                    }
                }
            }
        }
    }
}