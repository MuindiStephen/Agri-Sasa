package com.steve_md.smartmkulima.ui.fragments.merchant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.merchant.ui.fragment_adapter.FragmentAdapter
import com.ekenya.rnd.merchant.ui.fragments.events.Event
import com.ekenya.rnd.merchant.ui.fragments.events.EventsFragment
import com.ekenya.rnd.merchant.ui.fragments.tickets.TicketsFragment
import com.steve_md.smartmkulima.databinding.FragmentEventsTicketsMerchantBinding


class EventsAndTicketsFragment : Fragment() {

    private lateinit var binding : FragmentEventsTicketsMerchantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEventsTicketsMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {

        val fragmentsAdapter = FragmentAdapter(childFragmentManager).apply {
            addFragment(EventsFragment{navigateSelectedEvent(it)}, "Events")
            addFragment(TicketsFragment{navigateSelectedTicket(it)}, "Tickets")
        }

        binding.apply {
            viewPager.adapter = fragmentsAdapter
            tabLayout.setupWithViewPager(viewPager)
        }

    }

    private fun navigateSelectedTicket(event: Event) {
        val bundle = Bundle()
        bundle.putParcelable("event",event)
       // findNavController().navigate(R.id.action_eventsAndTicketsFragment_to_selectedTicketFragment,bundle)
    }

    private fun navigateSelectedEvent(event: Event) {
        val bundle = Bundle()
        bundle.putParcelable("event",event)
       // findNavController().navigate(R.id.action_eventsAndTicketsFragment_to_selectedEventFragment,bundle)
    }


}