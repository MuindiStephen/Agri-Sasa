package com.steve_md.smartmkulima.adapter.others

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.CropCycleTaskRowBinding
import com.steve_md.smartmkulima.model.LocalFarmCycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 *
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

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(cycle: LocalFarmCycle?) {
            binding.farmID.text = "${cycle?.farmName}"
            binding.cycleData.text = "${cycle?.cropName}"
            binding.dateForCycle.text = "${cycle?.startDate}"
            binding.textView85.text = "."
            binding.textViewCropCycleStatus.text = updateCropCycleStatus(cycle!!)

            // Attach the crop name to the item view for later use in the notification
            itemView.tag = cycle?.cropName
        }
    }

    // Crop Cycle status
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCropCycleStatus(localFarmCycle: LocalFarmCycle): String {

        val formatter = DateTimeFormatter.ofPattern("d-M-yyyy") // Updated to allow single-digit day/month
        val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy")


        val currentDate = LocalDate.now() // Get the current date
        val startDate = LocalDate.parse(localFarmCycle.startDate,formatter) // Parse the start date
        val endDate = LocalDate.parse(localFarmCycle.tasks.last().endDate,formatter2) // up to the very last crop cycle end date

        return when {
            currentDate.isBefore(startDate) -> "Upcoming"
            currentDate.isAfter(endDate) && localFarmCycle.status != "Done" -> "Overdue"
            currentDate.isAfter(startDate) && currentDate.isBefore(endDate) -> "In Progress"
            else -> localFarmCycle.status
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CropCycleTaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
