package com.steve_md.smartmkulima.adapter.others

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.DetailCycleRowBinding
import com.steve_md.smartmkulima.model.LocalTasks

/**
 * Able to view actual tasks for cycle - respective tasks
 */
class LocalFarmCycleTasksAdapter : RecyclerView.Adapter<LocalFarmCycleTasksAdapter.TaskViewHolder>() {

    private var tasks: List<LocalTasks> = ArrayList()

    inner class TaskViewHolder(private val binding: DetailCycleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: LocalTasks) {
            binding.textView75.text = task.taskName
            binding.textView76.text = task.startDate
            binding.textView77.text = task.endDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            DetailCycleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(taskList: List<LocalTasks>) {
        tasks = taskList
        notifyDataSetChanged()
    }
}
