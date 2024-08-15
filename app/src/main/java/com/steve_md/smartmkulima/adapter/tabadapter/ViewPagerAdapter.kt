package com.steve_md.smartmkulima.adapter.tabadapter

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.AutoCreateCropCycleFragment
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.CropCycleTasksListFragment

class VPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AutoCreateCropCycleFragment()
            1 -> CropCycleTasksListFragment()
            else -> {
                throw IllegalArgumentException("Invalid fragment position: $position")
            }
        }
    }
}