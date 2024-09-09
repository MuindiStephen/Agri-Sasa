package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgroDealersOffersListAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewAgroDealerInDetailBinding
import com.steve_md.smartmkulima.model.AgroDealerOffers

/**
 * Viewing Agro-Dealer in Detail
 */
class ViewAgroDealerInDetailFragment : Fragment() {

    private lateinit var binding: FragmentViewAgroDealerInDetailBinding
    private val args: ViewAgroDealerInDetailFragmentArgs by navArgs()

    private val agroDealersOffersListAdapter by lazy { AgroDealersOffersListAdapter { agrodealerOffer ->
        onOfferClicked(agrodealerOffer)
     }
    }


    // Navigate to My cart
    private fun onOfferClicked(agrodealerOffer: AgroDealerOffers) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAgroDealerInDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        val agrodealer = args.agrodealer

        binding.apply {
            textViewAgroDealerNameDetail.text = agrodealer.name
            textViewAgroDealerServices.text = agrodealer.servicesOffered
            textViewAgroDealerCategories.text = agrodealer.categories
        }

        // AgroDealers' Offers List
        binding.apply {
            agrodealer.offers.let {
                agroDealersOffersListAdapter.submitList(it)
            }

            offersListAgroDealersRV.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = agroDealersOffersListAdapter
            }
        }
    }
}