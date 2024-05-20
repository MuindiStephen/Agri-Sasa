package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.DetailCycleRowBinding
import com.steve_md.smartmkulima.model.GAPtask
import com.steve_md.smartmkulima.model.Tasks

class DetailGapAdapter : RecyclerView.Adapter<DetailGapAdapter.TaskViewHolder>() {

    private var gaps: List<GAPtask> = ArrayList()

    inner class TaskViewHolder(private val binding: DetailCycleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gap: GAPtask) {
           binding.textView75.text = gap.taskName
            binding.textView76.text = gap.startDate
            binding.textView77.text = gap.endDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            DetailCycleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = gaps[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return gaps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(gapList: List<GAPtask>) {
        gaps = gapList
        notifyDataSetChanged()
    }
}
