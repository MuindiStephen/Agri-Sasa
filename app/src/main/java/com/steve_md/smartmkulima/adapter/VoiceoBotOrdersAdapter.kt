package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.VoicebotOrdersItemRowBinding
import com.steve_md.smartmkulima.model.voicebotresponse.Order

/**
 * Voice-Bot-Orders-Adapter
 */
class VoiceBotOrdersAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<Order, VoiceBotOrdersAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
    inner class MyViewHolder(val binding: VoicebotOrdersItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order?) {
            binding.textView163.text = "Order ID: "+order?.orderId.toString()
            binding.textView165.text = "Payment Method: "+order?.paymentMethod
            binding.textView166.text = "Order Date: "+order?.orderDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            VoicebotOrdersItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(order = order)
        }

        holder.binding.root.setOnClickListener {
            onClickListener.onClick(order!!)
        }

    }

    class OnClickListener(val clickListener: (order: Order) -> Unit) {
        fun onClick(order: Order) = clickListener(order)
    }
}