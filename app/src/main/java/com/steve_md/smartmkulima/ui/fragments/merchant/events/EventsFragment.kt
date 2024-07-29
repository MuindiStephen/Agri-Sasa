package com.ekenya.rnd.merchant.ui.fragments.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.merchant.R
import com.ekenya.rnd.merchant.databinding.FragmentEventsMerchantBinding
import com.ekenya.rnd.merchant.ui.bottom_sheets.BottomSheetFilter

class EventsFragment(private val navigateToSelectedTicket : (event: Event) -> Unit ) : Fragment() {

    private lateinit var binding : FragmentEventsMerchantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEventsMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setUpCategoriesAdapter()
        setUpEventsAdapter()
        setUpBindings()
    }

    private fun setUpBindings() {
        binding.imageViewFilter.setOnClickListener {
            showFilterBottomSheet()
        }
    }

    private fun setUpEventsAdapter() {
        binding.recyclerViewTodayEvents.apply {

            layoutManager = LinearLayoutManager(
                requireContext())

            adapter = EventAdapter{onItemClick(it)}.apply {
                submitList(getEvents())
            }

        }
    }

    private fun onItemClick(event: Event) {
            navigateToSelectedTicket(event)
    }

    private fun getEvents() = mutableListOf<Event>().apply {
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_event))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_event_1))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_image3))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.event_example5))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_event))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_event_1))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.example_image3))
        add(Event("Sports Meet in Galaxy Field","Jan 12, 2019","Greenfields, Sector 42, Faridabad",R.drawable.event_example5))

    }

    private fun setUpCategoriesAdapter() {
        binding.recyclerViewCategories.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false)

            adapter = CategoriesAdapter().apply {
                submitList(getCategories())
            }

        }
    }

    private fun getCategories() = mutableListOf<EventCategory>().apply {
        add(EventCategory("Concert",R.drawable.ic_mic_merchant))
        add(EventCategory("Sport",R.drawable.ic_sports_merchant))
        add(EventCategory("Education",R.drawable.ic_graduation_merchant))
        add(EventCategory("Cinema",0))
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialogFragment: BottomSheetFilter = BottomSheetFilter ()
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

}