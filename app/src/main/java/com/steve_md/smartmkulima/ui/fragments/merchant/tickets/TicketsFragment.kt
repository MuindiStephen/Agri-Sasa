package com.ekenya.rnd.merchant.ui.fragments.tickets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.merchant.R
import com.ekenya.rnd.merchant.databinding.FragmentTicketsMerchantBinding
import com.ekenya.rnd.merchant.ui.fragments.events.Event
import com.ekenya.rnd.merchant.ui.fragments.events.EventAdapter

class TicketsFragment(private val navigateToSelectedTicket : (event: Event) -> Unit ) : Fragment() {

    private lateinit var binding: FragmentTicketsMerchantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTicketsMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEventsAdapter()
    }

    private fun setUpEventsAdapter() {

        binding.recyclerViewUpComingEvents.apply {

            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = EventAdapter { onItemClicked(it) }.apply {
                submitList(getEvents())
            }
        }

        binding.recyclerViewOtherEvents.apply {

            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = EventAdapter { onItemClicked(it) }.apply {
                submitList(getEvents())
            }
        }
    }

    private fun onItemClicked(event: Event) {
        navigateToSelectedTicket(event)
    }

    private fun getEvents() = mutableListOf<Event>().apply {
        add(
            Event(
                "Sports Meet in Galaxy Field ( 1 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_event
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 3 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_event_1
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 1 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_image3
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 6 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.event_example5
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 7 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_event
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 2 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_event_1
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 1 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.example_image3
            )
        )
        add(
            Event(
                "Sports Meet in Galaxy Field ( 9 Tickets)",
                "Jan 12, 2019",
                "Greenfields, Sector 42, Faridabad",
                R.drawable.event_example5
            )
        )

    }

}