package com.steve_md.smartmkulima.ui.fragments.main.fieldagents

import android.graphics.drawable.GradientDrawable.Orientation
import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmFieldsAdapter
import com.steve_md.smartmkulima.adapter.FieldAgentAddedAgroDealersAdapter
import com.steve_md.smartmkulima.databinding.FragmentFieldAgentDashboardBinding
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentAddAgroDealerData
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentEarnings
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.ViewCropCycleAnalyticsBottomSheetFragment
import com.steve_md.smartmkulima.utils.DateFormat
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class FieldAgentDashboardFragment : Fragment() {

    private lateinit var binding: FragmentFieldAgentDashboardBinding
    private lateinit var userProfileTxt: TextView
    private val viewModel: MainViewModel by viewModels()
    private val fieldAgentAddedAgroDealersAdapter by lazy {
        FieldAgentAddedAgroDealersAdapter()
    }
    private var fieldAgentAgroDealers =  mutableListOf<FieldAgentAddAgroDealerData>()

    private var fieldAgentEarnings: FieldAgentEarnings? = null

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

        setUpRecyclerView()

        fetchAllFieldAgentCreatedAgroDealers()
    }
    private fun fetchAllFieldAgentCreatedAgroDealers() {
        viewModel.allFieldAgentAddedAgroDealers.observe(viewLifecycleOwner) { agrodealers->
            if (!agrodealers.isNullOrEmpty()) {
                fieldAgentAgroDealers.clear()
                fieldAgentAgroDealers.addAll(agrodealers)
                fieldAgentAddedAgroDealersAdapter.submitList(fieldAgentAgroDealers)
                binding.noRecordsAgrodealers.isVisible = false

                binding.recyclerViewAddedAgrodealers.adapter = fieldAgentAddedAgroDealersAdapter
            } else {
                binding.noRecordsAgrodealers.isVisible = true
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewAddedAgrodealers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAddedAgrodealers.adapter = fieldAgentAddedAgroDealersAdapter
    }

    private fun setUpUi() {

        subscribeToFieldAgentEarningVMObservables()

        binding.apply {

            textViewEarnings.text = ""

            button4.setOnClickListener {
                showAddANewAgroDealerBottomSheetFragment()
            }

            button3.setOnClickListener {
                lifecycleScope.launch {
                    progressBar7.isVisible = true
                    delay(500L)
                    progressBar7.isVisible = false
                }
            }

        }

        binding.inputSearchAgrodealer.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideKeyboard()

                val searchText = binding.searchAgroDealer.editText?.text.toString().trim()

                if (searchText.isEmpty()) {
                    toast("Enter some text in order to search")
                }
                searchingAgroDealer(searchText)
                true
            } else {
                false
            }

        }

        binding.searchAgroDealer.setEndIconOnClickListener {
            hideKeyboard()

            if (binding.searchAgroDealer.editText?.text.isNullOrEmpty()) {
                return@setEndIconOnClickListener
            }
            binding.searchAgroDealer.editText?.setText("")
            fetchAllFieldAgentCreatedAgroDealers()
        }
    }

    private fun subscribeToFieldAgentEarningVMObservables() {
        viewModel.allFieldAgentEarnings.observe(viewLifecycleOwner) {
            it?.let {
                binding.textViewEarnings.text = "Current Earnings: ${it.points} points worth KSH. ${it.earnings}"
            }
        }
    }

    private fun showAddANewAgroDealerBottomSheetFragment() {
        val modal = AddANewAgroDealerBottomSheetFragment()
        modal.show(parentFragmentManager, TAG)
    }

    private fun searchingAgroDealer(searchText: String) {
        val filteredList = fieldAgentAgroDealers.filter { it.name.equals(searchText, ignoreCase = true) }
        fieldAgentAddedAgroDealersAdapter.submitList(filteredList.toMutableList())
    }

    override fun onResume() {
        super.onResume()
        displaySnackBar("Yaaay! Onboard more Agro-Dealers to increase your earnings")
    }

    companion object {
        const val TAG = "AddANewAgroDealerBottomSheetFragment"
    }
}