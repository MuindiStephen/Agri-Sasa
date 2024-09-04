package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.ItemRowFarmRevenuesRecordsBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords


class FarmRevenuesRecordsAdapter : ListAdapter<FarmFinanceRevenueRecords,
        FarmRevenuesRecordsAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<FarmFinanceRevenueRecords>() {
        override fun areItemsTheSame(oldItem: FarmFinanceRevenueRecords, newItem: FarmFinanceRevenueRecords): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: FarmFinanceRevenueRecords, newItem: FarmFinanceRevenueRecords): Boolean {
            return oldItem.id == newItem.id
        }
    }
    inner class MyViewHolder(private val binding: ItemRowFarmRevenuesRecordsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(farmRevenues: FarmFinanceRevenueRecords?) {
            binding.textView118.text = farmRevenues?.cropCycleName
            binding.textView120.text = farmRevenues?.harvestedBags
            binding.textView121.text = farmRevenues?.soldBags
            binding.textView122.text = farmRevenues?.revenueAfterSales
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRowFarmRevenuesRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val farmRevenues = getItem(position)
        holder.bind(farmRevenues)
    }
}