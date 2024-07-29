package com.ekenya.rnd.merchant.ui.fragments.events

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.merchant.databinding.ItemEventBinding
import kotlinx.android.parcel.Parcelize

class EventAdapter(private val onclick : (event : Event) -> Unit) : ListAdapter<Event, EventAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        //bind card details
        holder.itemCategoriesBinding.apply {
            holder.itemCategoriesBinding.apply {
                textViewEventName.text = event.title
                imageView14.setImageResource(event.image)
                root.setOnClickListener {
                    onclick(event)
                }
            }
        }
    }

    class ViewHolder(val itemCategoriesBinding: ItemEventBinding) :
        RecyclerView.ViewHolder(itemCategoriesBinding.root)

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}

@Parcelize
data class Event(
    val title: String,
    val date: String,
    val location: String,
    val image: Int
): Parcelable