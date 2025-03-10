package com.steve_md.smartmkulima.ui.fragments.twilio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.OrderItemsVoiceBotAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.FragmentVoiceOrderInDetailBinding
import com.steve_md.smartmkulima.model.voicebotresponse.Order
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoiceOrderInDetailFragment : Fragment() {

    private lateinit var binding: FragmentVoiceOrderInDetailBinding
    //private val args: VoiceOrderInDetailFragmentArgs by navArgs()

    private lateinit var orderItem: Order


    private val orderItemsAdapter by lazy { OrderItemsVoiceBotAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentVoiceOrderInDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

       // val orderItem = args.orderitem
        // Retrieve the Order object from arguments
        orderItem = arguments?.getParcelable("orderitem") ?: return

        binding.apply {
            textView163.text = "Order Id: " + orderItem.orderId.toString()
            textView165.text = "Payment Method: " + orderItem.orderId.toString()
            textView166.text = "Order date: " + orderItem.orderDate

        }


        binding.apply {

            // orderItemsAdapter.submitList(orderItem.items)

            orderItem.let {
                orderItemsAdapter.submitList(it.items)
            }

            rVOrderInDetail.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = orderItemsAdapter
            }
        }
    }
}