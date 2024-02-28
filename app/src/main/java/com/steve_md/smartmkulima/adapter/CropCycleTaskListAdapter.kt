package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.CropCycleTask

class CropCycleTaskListAdapter :
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
        @SuppressLint("SetTextI18n")
        fun bind(task: CropCycleTask?) {
            binding.textViewCropCycleName.text = "${task?.taskName} ${task?.selectedCrop} F1 CYCLE"
            binding.textViewCropName.text = "Crop: ${task?.selectedCrop}"
            binding.startDay.text = "Start Day: ${task?.taskStartDate.toString()}"
            binding.endDay.text = "End day: ${task?.taskEndDate.toString()}"
            binding.farmInput.text = "Farm Input: ${task?.farmInputRequired}"
            binding.priority.text = "Priority: ${task?.taskPriority}"
            binding.taskStatusName.text = "Task Name: ${task?.taskStatus}"

            binding.taskStatusName.setOnClickListener {
                val updatedStatus = getNextStatus(task?.taskStatus)
                task?.taskStatus = updatedStatus
                notifyItemChanged(adapterPosition)



               // task?.selectedCrop?.let { it1 -> databaseRef.child(it1).child("taskStatus").setValue(updatedStatus) }
            }

            when (task?.taskStatus) {
                "UPCOMING" -> binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.violet)
                )
                "COMPLETED" -> binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                "IN_PROGRESS" -> binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.blue)
                )
                else -> binding.colorIndicatorTaskStatus.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
            }
            // Now map status string to corresponding color resource ID


        }

        private fun getNextStatus(taskStatus: String?): String {
            return when (taskStatus) {
                "UPCOMING" -> "IN_PROGRESS"
                "IN_PROGRESS" -> "COMPLETED"
                "COMPLETED" -> "UPCOMING"
                else -> "UPCOMING" // Default to UPCOMING if status is unknown
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