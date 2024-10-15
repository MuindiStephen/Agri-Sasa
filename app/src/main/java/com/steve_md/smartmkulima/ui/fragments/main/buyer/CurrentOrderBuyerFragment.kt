package com.steve_md.smartmkulima.ui.fragments.main.buyer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.BuyerCartAdapter
import com.steve_md.smartmkulima.databinding.FragmentCurrentOrderBuyerBinding
import com.steve_md.smartmkulima.utils.OverlayService
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Buyer - track all current order
 */
@AndroidEntryPoint
class CurrentOrderBuyerFragment : Fragment() {

    private lateinit var binding: FragmentCurrentOrderBuyerBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var buyerCartAdapter: BuyerCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentOrderBuyerBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /**Getting all cart items */
        mainViewModel.cartLineItems.observe(viewLifecycleOwner) { cartItem ->
            buyerCartAdapter.submitList(cartItem)

            checkCartQuantity()
        }

        /**
         * Handling deleting an item from the cart
         */
        buyerCartAdapter = BuyerCartAdapter(BuyerCartAdapter.OnClickListener { cartItem->
            mainViewModel.removeOnlyOneItemFromCartLine(cartItem)
        })

    }

    private fun checkCartQuantity() {
        if (buyerCartAdapter.itemCount == 0) {
            toast("Cart is empty")

            binding.cartLineItemsRecyclerView.visibility = View.GONE

            binding.cartLineItemsRecyclerView.removeView(view)

            binding.noCart.isVisible = true

            binding.buttonCheckoutCartItems.isEnabled = false

        } else {
            toast("Items in your cart ready for checkout.")
            binding.cartLineItemsRecyclerView.adapter = buyerCartAdapter
            view?.findViewById<ImageView>(R.id.imageButtonDecrease)?.visibility = View.GONE
            view?.findViewById<ImageView>(R.id.imageButtonIncrease)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.textViewCartValue)?.visibility = View.GONE

            binding.buttonCheckoutCartItems.isEnabled = true
            binding.buttonCheckoutCartItems.setOnClickListener {

                // Initiate payment processing here.
                // check permission for overlay over other apps.
                checkOverlayPermission()
            }
        }
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                // Permission is not granted, request it
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))

                startActivityForResult(intent,
                    REQUEST_CODE_OVERLAY_PERMISSION
                )
            } else {
                // Permission is granted, show the overlay
                showOverLay()
            }
        } else {
            // For devices below API 23, permission is granted by default
            showOverLay()
        }
    }

    private fun showOverLay() {
        val intent = Intent(requireContext(), OverlayService::class.java)
        requireContext().startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(requireContext())) {
                    // Permission is granted
                    showOverLay()

                    displaySnackBar("Permitted to overlay over other apps")
                } else {
                    displaySnackBar("No permission to overlay over other apps")
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_OVERLAY_PERMISSION = 1
    }


}