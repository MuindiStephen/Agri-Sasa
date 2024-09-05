package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.ItemAnalyticsRowRecordBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary
import dagger.hilt.android.AndroidEntryPoint


/**
 * FarmAnalyticsRecordListAdapter
 */


class FarmAnalyticsRecordListAdapter( private val onClickListener: OnClickListener) :
    ListAdapter<FarmFinancialDataSummary, FarmAnalyticsRecordListAdapter.MyViewHolder>(MyDiffUtil) {

    object MyDiffUtil : DiffUtil.ItemCallback<FarmFinancialDataSummary>() {
        override fun areItemsTheSame(oldItem: FarmFinancialDataSummary, newItem: FarmFinancialDataSummary): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: FarmFinancialDataSummary, newItem: FarmFinancialDataSummary): Boolean {
            return oldItem.id == newItem.id
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class MyViewHolder(private val binding: ItemAnalyticsRowRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(summary: FarmFinancialDataSummary?) {
            binding.textView109.text = summary?.nameOfCropCycle.toString()
            binding.textView119.text = "Expenses: Kes. ${summary?.totalInputCosts.toString()}"
            binding.textView124.text = "Revenues: Kes. ${summary?.totalRevenueGenerated.toString()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAnalyticsRowRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = getItem(position)
        holder.bind(summary)

        /**
         *
         * Click to invoke this mtd
         */
        holder.itemView.setOnClickListener {
            onClickListener.onClick(summary = summary)
        }
    }

    class OnClickListener(val clickListener: (summary: FarmFinancialDataSummary) -> Unit) {
        fun onClick(summary: FarmFinancialDataSummary) = clickListener(summary)
    }
}