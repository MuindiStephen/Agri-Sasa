package com.steve_md.smartmkulima.ui.fragments.main.buyer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentMarketProduceInDetailsBinding
import com.steve_md.smartmkulima.model.BuyerCart
import com.steve_md.smartmkulima.utils.OverlayService
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideSupportActionBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Market produce click to view in details as a buyer
 */
@AndroidEntryPoint
class MarketProduceInDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMarketProduceInDetailsBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketProduceInDetailsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSupportActionBar()

        Glide.with(binding.imageView32)
            .load(arguments?.getString("productImage"))
            .centerCrop()
            .into(binding.imageView32)
        binding.textView175.text = arguments?.getString("productTitle")
        binding.textView176.text = ""+arguments?.getString("productDescription")


        val productItem = arguments?.getString("productImage")?.let {
            BuyerCart(
                productImage = it,
                productTitle = arguments?.getString("productTitle")!!,
                productPrice = arguments?.getString("productDescription")!!
            )
        }

        binding.addToCart.setOnClickListener {

            if (productItem != null) {
                viewModel.insertItemToCartLine(productItem)
            }

            displaySnackBar("Added Item to Cart")

            // Navigate To my cart buyer
            findNavController().navigate(R.id.myCartBuyerFragment)
        }
    }
}