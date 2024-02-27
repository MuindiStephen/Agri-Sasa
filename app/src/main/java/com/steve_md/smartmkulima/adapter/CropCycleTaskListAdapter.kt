package com.steve_md.smartmkulima.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.CropCycleTask

class CropCycleTaskListAdapter  :
    ListAdapter<CropCycleTask, CropCycleTaskListAdapter.MyViewHolder>(TaskDiffUtil) {
    object TaskDiffUtil : DiffUtil.ItemCallback<CropCycleTask>(){
        override fun areItemsTheSame(oldItem: CropCycleTask, newItem: CropCycleTask): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CropCycleTask, newItem: CropCycleTask): Boolean {
            return oldItem.taskName == newItem.taskName
        }

    }
    inner class MyViewHolder(private var binding: CropCycleTaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: CropCycleTask?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            CropCycleTaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}