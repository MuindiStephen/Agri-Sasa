package com.ekenya.rnd.merchant.ui.fragments.deals.deals_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ekenya.rnd.merchant.databinding.ItemDealMerchantBinding

class MyDealRecyclerViewAdapter : ListAdapter<Deal, MyDealRecyclerViewAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemDealMerchantBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)
        //bind card details
        holder.cardBankcarditemBinding.apply {
            textViewProductName.text = option.name
            textViewActualPrice.text = option.price
            textViewDiscountPercentage.text = option.discountPercentage
            textViewDiscountedPrice.text = option.price
        }
    }


    class ViewHolder(val cardBankcarditemBinding: ItemDealMerchantBinding) : RecyclerView.ViewHolder(cardBankcarditemBinding.root)



}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<Deal>() {
    override fun areItemsTheSame(oldItem: Deal, newItem: Deal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Deal, newItem: Deal): Boolean {
        return oldItem == newItem
    }

}

data class Deal(
    val name : String,
    val price : String,
    val discountPercentage: String,
    val discountPrice : String,
    )
