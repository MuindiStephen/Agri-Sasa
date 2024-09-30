package com.steve_md.smartmkulima.ui.fragments.main.fieldagents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentFieldAgentDashboardBinding
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.ViewCropCycleAnalyticsBottomSheetFragment
import com.steve_md.smartmkulima.utils.DateFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class FieldAgentDashboardFragment : Fragment() {

    private lateinit var binding: FragmentFieldAgentDashboardBinding
    private lateinit var userProfileTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentFieldAgentDashboardBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileTxt = view.findViewById<TextView>(R.id.userNameTextView)

        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }

        binding.textViewLastLoggedInTimeDate.text = DateFormat.getCurrentDate()

        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            button4.setOnClickListener {
                showAddANewAgroDealerBottomSheetFragment()
            }
            button3.setOnClickListener {

                // show progress bar loading...
                // shuffle recycler view Items
            }
        }
    }

    private fun showAddANewAgroDealerBottomSheetFragment() {
        val modal = AddANewAgroDealerBottomSheetFragment()
        modal.show(parentFragmentManager, TAG)
    }

    companion object {
        const val TAG = "AddANewAgroDealerBottomSheetFragment"
    }
}