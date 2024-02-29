package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.CropCycleTask
import timber.log.Timber


class CropCycleTaskListAdapter(
    private val databaseReference: DatabaseReference,
    private val context: Context
    ) :
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

                val dialogBuilder = AlertDialog.Builder(binding.root.context)
                dialogBuilder.setMessage("Are you sure you want to change the status?")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_cycle)
                    .setTitle("NEW TASK STATUS FOR ${task?.selectedCrop} ${task?.taskName} F1 CYCLE")
                    .setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        handleStatusChange(task)
                        updateColorIndicator(task)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = dialogBuilder.create()
                alert.show()

            }
        }
        private fun updateColorIndicator(task: CropCycleTask?) {
            // Now map status string to corresponding color resource ID
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
        }
        private fun handleStatusChange(task: CropCycleTask?) {
            // Update the local UI
            val updatedStatus = getNextStatus(task?.taskStatus)
            task?.taskStatus = updatedStatus
            notifyItemChanged(adapterPosition)

            // Now update also the firebase database
            val taskRef = databaseReference.child("crop_cycle_tasks")
            val matchingCropCycleTask = taskRef.orderByChild("taskName").equalTo(task?.taskName)
            matchingCropCycleTask.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(tasksSnapshot: DataSnapshot) {
                    for (snapshot in tasksSnapshot.children) {
                        snapshot.ref.child("taskStatus").setValue(updatedStatus)
                        Timber.d("Mapped ${task?.taskStatus} status to $updatedStatus status for ${task?.taskName}${task?.selectedCrop} F1 CYCLE" )

                        Toast.makeText(context, "Updated status", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Timber.v("Failed to update status {}", error.message)
                }
            })

        }

        private fun getNextStatus(taskStatus: String?): String {
            return when (taskStatus) {
                "UPCOMING" -> "IN_PROGRESS"
                "IN_PROGRESS" -> "COMPLETED"
                "COMPLETED" -> "${!binding.taskStatusName.isClickable}"
                else -> "${!binding.taskStatusName.isClickable}" // Default to UPCOMING if status is unknown
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