package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgroDealersOffersListAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewAgroDealerInDetailBinding
import com.steve_md.smartmkulima.model.AgroDealerOffers
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Viewing Agro-Dealer in Detail
 */
@AndroidEntryPoint
class ViewAgroDealerInDetailFragment : Fragment() {

    private lateinit var binding: FragmentViewAgroDealerInDetailBinding
    private val args: ViewAgroDealerInDetailFragmentArgs by navArgs()
    private val viewModel:MainViewModel by activityViewModels()

    private val agroDealersOffersListAdapter by lazy {
        AgroDealersOffersListAdapter { agrodealerOffer ->
        onOfferClicked(agrodealerOffer)
     }
    }


    // Navigate to My cart and checkout
    private fun onOfferClicked(agrodealerOffer: AgroDealerOffers) {

        /**
         * For this case we associate AgroDealer unique ID with each transaction
         * When adding items to cart. To ensure AgroDealer's ID is passed along with the offer
         * details when an item is added to cart
         */
        val agroDealerUniqueID = args.agrodealer.id



        viewModel.addToCart(agrodealerOffer)
        displaySnackBar("Item Added To Cart.")
        findNavController().navigate(R.id.myCartAgroDealerInputsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAgroDealerInDetailBinding.inflate(
            inflater, container, false
        )
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