package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgroDealsCartItemsListAdapter
import com.steve_md.smartmkulima.databinding.FragmentMyCartAgroDealerInputsBinding
import com.steve_md.smartmkulima.model.FarmEquipment
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * My Cart / Provides a list of all cart items ready for check-out
 */
@AndroidEntryPoint
class MyCartAgroDealerInputsFragment : Fragment() {

    private lateinit var binding: FragmentMyCartAgroDealerInputsBinding
    private lateinit var cartAdapter: AgroDealsCartItemsListAdapter
    private val viewModel: MainViewModel by activityViewModels()
   // private var cartListItems = mutableListOf<FarmInputAgroDealerCartItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMyCartAgroDealerInputsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        setUpRecyclerViewAndCart()

        loadCartItemsAndCalculateCheckout()
    }


    private fun setUpRecyclerViewAndCart() {

        cartAdapter = AgroDealsCartItemsListAdapter(AgroDealsCartItemsListAdapter.OnClickListener{ cart: FarmInputAgroDealerCartItem ->
            Timber.i("Clicked: ${cart.offerProduct.productName}")

            // X remove item from cart
            viewModel.removeFromCart(cart.offerProduct)
            Timber.i("Item removed: ${viewModel.removeFromCart(cart.offerProduct)}")

            // (+) increasing quantity.
            view?.findViewById<ImageButton>(R.id.imageButtonIncrease)?.setOnClickListener {
                viewModel.increaseQuantity(cart.offerProduct)
            }
            // (-) decreasing quantity.
            view?.findViewById<ImageButton>(R.id.imageButtonDecrease)?.setOnClickListener {
                viewModel.decreaseQuantity(cart.offerProduct)
            }
        })

        binding.recyclerView4.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        Timber.tag(this@MyCartAgroDealerInputsFragment.toString())
            .d("Adapter has %d items", cartAdapter.itemCount)

    }



    private fun setUpUi() {
        
        binding.toolbarMyCart.setNavigationOnClickListener { 
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun loadCartItemsAndCalculateCheckout() {
        lifecycleScope.launch {
            viewModel.cart.collect { cartItems ->

                if (cartItems.isNotEmpty()) {
//                    cartListItems.clear()
//                    cartListItems.addAll(cartItems)
                    cartAdapter.submitList(cartItems)
                    cartAdapter.notifyDataSetChanged()
                    binding.recyclerView4.adapter = cartAdapter

                    Timber.tag(this@MyCartAgroDealerInputsFragment.toString())
                        .e("Fetched cart items successfully. $cartItems" +
                                "\tNumber of Cart Items Found==${cartItems.size}")

                    binding.noCart.isVisible = false
                    binding.recyclerView4.isVisible = true

                    // Calculating checkout price
                    val totalCheckoutPrice = cartItems.sumOf {
                        it.offerProduct.discountedPrice * it.quantity
                    }

                    Log.d("RecyclerViewVisibility", "RecyclerView isVisible: ${binding.recyclerView4.isVisible}")

                    binding.textViewSubTotal.text = String.format("%.2f",totalCheckoutPrice)
                    val checkoutValueIncludingFees = totalCheckoutPrice + 200.toDouble()
                    binding.textView139.text = String.format("%.2f",checkoutValueIncludingFees)

                    Log.d("MyCartCheckout-AgroDeals", "Cart items: ${cartItems.size}")
                    cartItems.forEach { item ->
                        Log.d("MyCartCheckout-AgroDeals", "${item.offerProduct.productName} - Quantity: ${item.quantity}")
                    }

                    binding.buttonCheckoutCartItems.isEnabled = true

                    val bundle = Bundle().apply {
                        putInt("TOTAL_PRICE", totalCheckoutPrice.toInt())
                    }
                    binding.buttonCheckoutCartItems.setOnClickListener {
                        findNavController().navigate(R.id.paymentFragment,bundle)
                    }

                } else {
                    Timber.tag(this@MyCartAgroDealerInputsFragment.toString())
                        .e("No Cart Items. $cartItems")
                    binding.noCart.isVisible = true
                    binding.recyclerView4.isVisible = false
                    binding.textViewSubTotal.isVisible = false
                    binding.textView139.isVisible = false
                    binding.buttonCheckoutCartItems.isEnabled = false
                }
            }
        }
    }
}