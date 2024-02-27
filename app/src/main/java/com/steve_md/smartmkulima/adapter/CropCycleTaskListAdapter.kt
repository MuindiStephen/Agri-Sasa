package com.steve_md.smartmkulima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.steve_md.smartmkulima.R
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
            binding.textViewCropName.text = task?.selectedCrop
            binding.startDay.text = task?.taskStartDate.toString()
            binding.endDay.text = task?.taskEndDate.toString()
            binding.farmInput.text = task?.farmInputRequired
            binding.priority.text = task?.taskPriority
            binding.taskStatusName.text = task?.taskStatus

            // Now map status string to corresponding color resource ID
            if (task?.taskStatus == "UPCOMING")
            {
                binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context,R.color.violet)
                )
            }
            else if (task?.taskStatus == "COMPLETED") {
                binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context,R.color.green)
                )
            }
            else if (task?.taskStatus == "IN_PROGRESS") {
                binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context,R.color.blue)
                )
            }
            else {
                binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context,R.color.red)
                )
            }

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