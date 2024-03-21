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

class CropCycleTaskListAdapter :
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
            binding.farmID.text = cycle?.type
            binding.cycleData.text = "Starts: ${cycle?.startDate}"
            binding.dateForCycle.text = "ends: ${cycle?.endDate}"
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
    }
}
