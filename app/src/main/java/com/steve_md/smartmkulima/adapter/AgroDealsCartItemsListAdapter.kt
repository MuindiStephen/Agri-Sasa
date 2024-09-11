package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.ItemDealAgrodealerAddToCartBinding
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem


/**
 * Will Attach to the Ui all the cart items stored temporary in the ViewModel
 */
class AgroDealsCartItemsListAdapter (private val onClickListener: OnClickListener)
    : ListAdapter<FarmInputAgroDealerCartItem, AgroDealsCartItemsListAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<FarmInputAgroDealerCartItem>() {
        override fun areItemsTheSame(oldItem: FarmInputAgroDealerCartItem, newItem: FarmInputAgroDealerCartItem): Boolean {
            return oldItem.offerProduct.id == newItem.offerProduct.id
        }

        override fun areContentsTheSame(oldItem: FarmInputAgroDealerCartItem, newItem: FarmInputAgroDealerCartItem): Boolean {
            return oldItem == newItem
        }

    }
    inner class MyViewHolder(private val binding: ItemDealAgrodealerAddToCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val btnRemoveItemFromCart = binding.buttonRemoveCartItem
        @SuppressLint("SetTextI18n")
        fun bind(offerAddedToCart: FarmInputAgroDealerCartItem?) {
            binding.textViewProductName.text = offerAddedToCart?.offerProduct?.productName
            binding.textViewProductPriceAfterDiscount.text = "Kes. "+offerAddedToCart?.offerProduct?.discountedPrice.toString()
            binding.textViewDiscountPercentage.text = offerAddedToCart?.offerProduct?.discountPercentage

            Glide.with(binding.imageViewProductFarmInputName)
                .load(offerAddedToCart?.offerProduct?.productImageResId)
                .centerCrop()
                .into(binding.imageViewProductFarmInputName)

            binding.textViewCartValue.text = offerAddedToCart?.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemDealAgrodealerAddToCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val offerAddedToCart = getItem(position)
        holder.bind(offerAddedToCart)

        holder.btnRemoveItemFromCart.setOnClickListener {
            onClickListener.onClick(offerAddedToCart)
        }
    }

    class OnClickListener(val clickListener: (cart: FarmInputAgroDealerCartItem) -> Unit) {
        fun onClick(cart: FarmInputAgroDealerCartItem) = clickListener(cart)
    }
}