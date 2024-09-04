package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.ItemFarmRecordRowBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords

class FarmExpensesRecordsAdapter : ListAdapter<FarmFinanceExpenseRecords,
        FarmExpensesRecordsAdapter.MyViewHolder>(MyDiffUtil) {
    object MyDiffUtil : DiffUtil.ItemCallback<FarmFinanceExpenseRecords>() {
        override fun areItemsTheSame(oldItem: FarmFinanceExpenseRecords, newItem: FarmFinanceExpenseRecords): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FarmFinanceExpenseRecords, newItem: FarmFinanceExpenseRecords): Boolean {
            return oldItem.id == newItem.id
        }

    }
    inner class MyViewHolder(private val binding: ItemFarmRecordRowBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(farmExpenses: FarmFinanceExpenseRecords?) {
            binding.textView118.text = farmExpenses?.nameOfCropCycle
            binding.textView120.text = farmExpenses?.nameOfExpense
            binding.textView121.text = farmExpenses?.whichTask
            binding.textView122.text = farmExpenses?.dateOfThisFinancialRecord
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemFarmRecordRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val farmExpenses = getItem(position)
        holder.bind(farmExpenses)
    }
}