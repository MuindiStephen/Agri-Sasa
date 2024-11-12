package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.CartItemRowBinding
import com.steve_md.smartmkulima.databinding.ItemBuyerCartCheckoutItemsBinding
import com.steve_md.smartmkulima.databinding.ItemDealAgrodealerAddToCartBinding
import com.steve_md.smartmkulima.model.BuyerCart
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem

/***
 * BuyerCartAdapter
 */
class BuyerCartAdapter (
    private val onClickListener: OnClickListener
) : ListAdapter<BuyerCart, BuyerCartAdapter.CartViewHolder>(CartDiffUtil) {
    object CartDiffUtil : DiffUtil.ItemCallback<BuyerCart>() {
        override fun areItemsTheSame(oldItem: BuyerCart, newItem: BuyerCart): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BuyerCart, newItem: BuyerCart): Boolean {
            return oldItem.cartId == newItem.cartId
        }

    }

    inner class CartViewHolder(private val binding: ItemBuyerCartCheckoutItemsBinding) :  RecyclerView.ViewHolder(binding.root) {

        val deleteItemFromCartLine = binding.buttonRemoveCartItem
        val btnIncreaseQ = binding.imageButtonIncrease
        val btnDecreaseQ = binding.imageButtonDecrease

        @SuppressLint("SetTextI18n")
        fun bind(cartLineItem: BuyerCart?) {
            Glide.with(binding.imageViewProductFarmInputName).load(cartLineItem?.productImage)
                .centerCrop()
                .fitCenter()
                .into(binding.imageViewProductFarmInputName)

            binding.textViewProductName.text = cartLineItem?.productTitle
            binding.textViewProductPriceAfterDiscount.text = "" + cartLineItem?.productPrice
            binding.textViewCartValue.text = cartLineItem?.quantity.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemBuyerCartCheckoutItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartLineItem = getItem(position)
        holder.bind(cartLineItem)

        holder.deleteItemFromCartLine.setOnClickListener {
            onClickListener.onClick(cartLineItem)
        }

        holder.btnIncreaseQ.setOnClickListener {
            onClickListener.onIncreaseQuantity(cartLineItem)
        }

        holder.btnDecreaseQ.setOnClickListener {
            onClickListener.onDecreaseQuantity(cartLineItem)
        }
    }

    class OnClickListener (
        val clickListener: (cart: BuyerCart) -> Unit,
        val increaseQuantity: (cartItem: BuyerCart) -> Unit,
        val decreaseQuantity: (cartItem: BuyerCart) -> Unit
    ) {
        fun onClick(cart: BuyerCart) = clickListener(cart)
        fun onIncreaseQuantity(cart: BuyerCart) = increaseQuantity(cart)
        fun onDecreaseQuantity(cart: BuyerCart) = decreaseQuantity(cart)
    }
}