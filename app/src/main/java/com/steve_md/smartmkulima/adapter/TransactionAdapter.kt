package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.TransactionRowBinding
import com.steve_md.smartmkulima.model.Transaction


class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.MyViewHolder>(MyDiffUtil) {

    object MyDiffUtil : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }
    }
    inner class MyViewHolder(private val binding: TransactionRowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction?) {
            binding.tvAmount.text = transaction?.amount.toString()
            binding.tvDate.text = transaction?.transactionDateTime.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }
}


