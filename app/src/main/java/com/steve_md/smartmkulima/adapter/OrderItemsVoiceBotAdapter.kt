package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.VoicebotOrderdetailsItemRowBinding
import com.steve_md.smartmkulima.model.voicebotresponse.OrderItem

class OrderItemsVoiceBotAdapter : RecyclerView.Adapter<OrderItemsVoiceBotAdapter.TaskViewHolder>() {

    private var orderItems: List<OrderItem> = ArrayList()

    inner class TaskViewHolder(private val binding: VoicebotOrderdetailsItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(orderItem: OrderItem) {
            binding.textView163.text = "Product Name: "+orderItem.productName
            binding.textView165.text = "Quantity: "+orderItem.quantity
            binding.textView166.text = "Total: " + orderItem.total.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            VoicebotOrderdetailsItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = orderItems[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(orderItemList: List<OrderItem>) {
        orderItems = orderItemList
        notifyDataSetChanged()
    }
}