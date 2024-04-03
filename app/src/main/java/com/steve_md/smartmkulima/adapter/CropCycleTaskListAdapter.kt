package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.CropCycleTask
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.FarmEquipment

class CropCycleTaskListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Cycle, CropCycleTaskListAdapter.MyViewHolder>(TaskDiffUtil) {

    object TaskDiffUtil : DiffUtil.ItemCallback<Cycle>() {
        override fun areItemsTheSame(oldItem: Cycle, newItem: Cycle): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cycle, newItem: Cycle): Boolean {
            return oldItem.type == newItem.type
        }
    }

    inner class MyViewHolder(private var binding: CropCycleTaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cycle: Cycle?) {
            binding.farmID.text = cycle?.farmId
            binding.cycleData.text = cycle?.cropName
            binding.dateForCycle.text = cycle?.startDate
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

    class OnClickListener(val clickListener: (cycle: Cycle) -> Unit) {
        fun onClick(cycle: Cycle) = clickListener(cycle)
    }
}
