package com.steve_md.smartmkulima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.ItemRowAgrodealerOrdersBinding
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer

class AgrodealerOrdersAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<OrderCheckoutByFarmer, AgrodealerOrdersAdapter.MyViewHolder>(MyDiffUtil) {

        private val context: Context? = null
    object MyDiffUtil : DiffUtil.ItemCallback<OrderCheckoutByFarmer>() {
        override fun areItemsTheSame(
            oldItem: OrderCheckoutByFarmer,
            newItem: OrderCheckoutByFarmer
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: OrderCheckoutByFarmer,
            newItem: OrderCheckoutByFarmer
        ): Boolean {
            return oldItem.orderId == newItem.orderId
        }

    }

    inner class MyViewHolder(private val binding: ItemRowAgrodealerOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderCheckoutByFarmer?) {
            binding.textView157.text = "Order No: "+ order?.orderId.toString()
            binding.textView158.text = "Total Ksh. "+order?.totalOrderInMoney.toString()
            binding.textView159.text = "Status: ${order?.orderStatus.toString()}"
            binding.textView160.text = "Farmer contact info: ${order?.farmEmail.toString()}"
            binding.textView161.text = "Farmer location: "+order?.farmerLocation.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRowAgrodealerOrdersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(orderCheckoutByFarmer = order)
        }
    }

    // Handling OnclickListener on recyclerview holder
    class OnClickListener(val clickListener: (orderCheckoutByFarmer: OrderCheckoutByFarmer) -> Unit) {
        fun onClick(orderCheckoutByFarmer: OrderCheckoutByFarmer) = clickListener(orderCheckoutByFarmer)
    }
}