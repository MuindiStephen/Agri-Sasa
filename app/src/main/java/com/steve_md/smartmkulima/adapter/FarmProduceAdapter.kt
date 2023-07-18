package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.ProductsRowBinding
import com.steve_md.smartmkulima.model.FarmProduce


class FarmProduceAdapter : ListAdapter<FarmProduce, FarmProduceAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<FarmProduce>() {
        override fun areItemsTheSame(oldItem: FarmProduce, newItem: FarmProduce): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FarmProduce, newItem: FarmProduce): Boolean {
            return oldItem.id == newItem.id
        }

    }
    inner class MyViewHolder(private val binding: ProductsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: FarmProduce?) {
            binding.productPrice.text = product?.productPrice
            binding.productTitle.text = product?.productTitle

            Glide.with(binding.productImageBox)
                .load(product?.productImageUrl)
                .centerCrop()
                .into(binding.productImageBox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProductsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}