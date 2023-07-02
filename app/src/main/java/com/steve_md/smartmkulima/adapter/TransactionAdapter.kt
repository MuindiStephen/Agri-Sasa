package com.steve_md.smartmkulima.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.Transaction

class TransactionAdapter(private val transactions: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_row, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val amountTextView: TextView = itemView.findViewById(R.id.tvAmount)
        private val transactionDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(transaction: Transaction) {
            transactionDate.text = transaction.transactionDateTime.toString()
            amountTextView.text = transaction.amount.toString()
        }
    }
}
