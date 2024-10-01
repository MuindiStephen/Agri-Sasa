package com.steve_md.smartmkulima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.ItemFarmerTrackOrdersToAgrodealersRowBinding
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer

/**
 * Adapter for attaching order data to the Ui (xml)
 */
class FarmersOrderToAgrodealersListAdapter :
    ListAdapter<OrderCheckoutByFarmer, FarmersOrderToAgrodealersListAdapter.MyViewHolder>(MyDiffUtil) {

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

    inner class MyViewHolder(private val binding: ItemFarmerTrackOrdersToAgrodealersRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderCheckoutByFarmer?) {
            binding.textView157.text = "# Order No: " + order?.orderId.toString()
            binding.textView158.text = "Total Ksh. " + order?.totalOrderInMoney.toString()
            binding.textView159.text = "Status: ${order?.orderStatus.toString()}"
            binding.textView160.text = "AgroDealer Location: ${order?.farmerLocation.toString()}"
            binding.textView161.text = "AgroDealer ID:  " + order?.agrodealerID.toString()

            if (order?.orderStatus.toString() == "Approved") {
                binding.textView171.isVisible = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemFarmerTrackOrdersToAgrodealersRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }
}