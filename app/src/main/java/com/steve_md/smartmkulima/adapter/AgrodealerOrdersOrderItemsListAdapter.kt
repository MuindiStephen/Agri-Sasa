package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.ItemAgrodealerViewOrdersInDetailBinding
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem

class AgrodealerOrdersOrderItemsListAdapter : ListAdapter<FarmInputAgroDealerCartItem, AgrodealerOrdersOrderItemsListAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<FarmInputAgroDealerCartItem>() {
        override fun areItemsTheSame(oldItem: FarmInputAgroDealerCartItem, newItem: FarmInputAgroDealerCartItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FarmInputAgroDealerCartItem, newItem: FarmInputAgroDealerCartItem): Boolean {
            return oldItem.offerProduct.id == newItem.offerProduct.id
        }

    }
    inner class MyViewHolder(private val binding: ItemAgrodealerViewOrdersInDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: FarmInputAgroDealerCartItem?) {
            binding.productPrice.text = "Quantity: " + product?.quantity.toString()
            binding.productTitle.text = product?.offerProduct?.productName.toString()

            Glide.with(binding.productImageBox)
                .load(product?.offerProduct?.productImageResId)
                .centerCrop()
                .into(binding.productImageBox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAgrodealerViewOrdersInDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}