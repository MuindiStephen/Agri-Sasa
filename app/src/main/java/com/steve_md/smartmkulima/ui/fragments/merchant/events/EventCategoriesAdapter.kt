package com.ekenya.rnd.merchant.ui.fragments.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.merchant.databinding.ItemEventCategoriesMerchantBinding

class CategoriesAdapter : ListAdapter<EventCategory, CategoriesAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventCategoriesMerchantBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        //bind card details
        holder.itemCategoriesBinding.apply {
            textViewCategory.text = category.title
            imageViewCategory.setImageResource(category.image)
        }
    }

    class ViewHolder(val itemCategoriesBinding: ItemEventCategoriesMerchantBinding) : RecyclerView.ViewHolder(itemCategoriesBinding.root)

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<EventCategory>() {
    override fun areItemsTheSame(oldItem: EventCategory, newItem: EventCategory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EventCategory, newItem: EventCategory): Boolean {
        return oldItem == newItem
    }

}

data class EventCategory(
    val title : String,
    val image : Int
)