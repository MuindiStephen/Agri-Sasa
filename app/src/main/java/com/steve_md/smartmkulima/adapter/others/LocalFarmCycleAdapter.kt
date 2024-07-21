package com.steve_md.smartmkulima.adapter.others

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.LocalFarmCycle

/**
 * Attach data to locally created Farm cycles to Room DB
 */
class LocalFarmCycleAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<LocalFarmCycle, LocalFarmCycleAdapter.MyViewHolder>(TaskDiffUtil) {

    object TaskDiffUtil : DiffUtil.ItemCallback<LocalFarmCycle>() {
        override fun areItemsTheSame(oldItem: LocalFarmCycle, newItem: LocalFarmCycle): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LocalFarmCycle, newItem: LocalFarmCycle): Boolean {
            return oldItem.cropName == newItem.cropName
        }
    }

    inner class MyViewHolder(private var binding: CropCycleTaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cycle: LocalFarmCycle?) {
            binding.farmID.text = cycle?.farmName
            binding.cycleData.text = cycle?.cropName
            binding.dateForCycle.text = cycle?.startDate
            binding.textView85.text = "Crop cycle"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CropCycleTaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cycle = getItem(position)
        holder.bind(cycle)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(cycle = cycle)
        }
    }

    class OnClickListener(val clickListener: (cycle: LocalFarmCycle) -> Unit) {
        fun onClick(cycle: LocalFarmCycle) = clickListener(cycle)
    }
}
