package com.ekenya.rnd.merchant.ui.fragments.deals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.merchant.databinding.ItemDealCategoryMerchantBinding

class DealCategoryAdapter(private val onItemClicked: () -> Unit) : ListAdapter<DealCategory, DealCategoryAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDealCategoryMerchantBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)
        //bind card details
        holder.cardBankcarditemBinding.apply {
            textViewTitle.text = option.title
            textViewSubTitle.text = option.subtitle
            buttonShopNow.setOnClickListener {
                onItemClicked()
            }
        }
    }

    class ViewHolder(val cardBankcarditemBinding: ItemDealCategoryMerchantBinding) : RecyclerView.ViewHolder(cardBankcarditemBinding.root)

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<DealCategory>() {
    override fun areItemsTheSame(oldItem: DealCategory, newItem: DealCategory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DealCategory, newItem: DealCategory): Boolean {
        return oldItem == newItem
    }

}

data class DealCategory(
    val title : String,
    val subtitle : String
)