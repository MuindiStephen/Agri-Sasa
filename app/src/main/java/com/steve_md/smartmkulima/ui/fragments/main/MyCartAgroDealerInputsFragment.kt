package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgroDealsCartItemsListAdapter
import com.steve_md.smartmkulima.databinding.FragmentMyCartAgroDealerInputsBinding
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
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
    private var cartListItems = mutableListOf<FarmInputAgroDealerCartItem>()
    private var firebaseAuth: FirebaseAuth? = null

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

        cartAdapter = AgroDealsCartItemsListAdapter(
            AgroDealsCartItemsListAdapter.OnClickListener(
                clickListener = { cart: FarmInputAgroDealerCartItem ->
                    Timber.i("Clicked: ${cart.offerProduct.productName}")
                    viewModel.removeFromCart(cart.offerProduct)
                    Timber.i("Item removed: ${viewModel.removeFromCart(cart.offerProduct)}")
                },
                increaseQuantity = { cart ->
                    viewModel.increaseQuantity(cart.offerProduct)
                    loadCartItemsAndCalculateCheckout()
                },
                decreaseQuantity = { cart ->
                    viewModel.decreaseQuantity(cart.offerProduct)
                    loadCartItemsAndCalculateCheckout()
                }
            )
        )

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
                    cartListItems.clear()
                    cartListItems.addAll(cartItems)
                    cartAdapter.submitList(cartListItems)
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

                    binding.textViewSubTotal.text = "Kes. " + String.format("%.2f",totalCheckoutPrice)
                    val checkoutValueIncludingFees = totalCheckoutPrice + 200.toDouble()
                    binding.textView139.text = "Kes. " + String.format("%.2f",checkoutValueIncludingFees)

                    Log.d("MyCartCheckout-AgroDeals", "Cart items: ${cartItems.size}")
                    cartItems.forEach { item ->
                        Log.d("MyCartCheckout-AgroDeals", "${item.offerProduct.productName} - Quantity: ${item.quantity}")
                    }

                    binding.buttonCheckoutCartItems.isEnabled = true

                    val bundle = Bundle().apply {
                        putInt("TOTAL_PRICE", checkoutValueIncludingFees.toInt())
                    }

                    binding.buttonCheckoutCartItems.setOnClickListener {

                        // save this order to backend side.
                        // farmers location
                        // farmer email
                        // agrodealer ID

                        firebaseAuth = FirebaseAuth.getInstance()
                        val currentUser = firebaseAuth!!.currentUser

                        if (currentUser != null) {
                            val userEmail = currentUser.email.toString()
                            Timber.d("Logged In Farmer => $userEmail")
                        }
                        else {
                            Timber.tag(YourAccountFragment.TAG).e("An error,occurred while retrieving your profile")
                        }

                        val orderCheckoutByFarmer = OrderCheckoutByFarmer(
                            cartOrder = cartListItems,
                            farmerLocation = arguments?.getString("distance")!!,
                            farmEmail = currentUser?.email.toString(),
                            agrodealerID = arguments?.getString("agrodealerID")!!,
                            orderStatus = "Pending",
                            totalOrderInMoney = checkoutValueIncludingFees.toString()
                        )

                        try {
                            viewModel.saveOrder(orderCheckoutByFarmer)
                            toast("Order placed successfully")
                        } catch (e: Exception) {
                            Timber.tag("Orders").e("Order Not Placed." +
                                    "ERROR==${e.message}")
                        }
                        findNavController().navigate(R.id.paymentFragment, bundle)
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