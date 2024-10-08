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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.steve_md.smartmkulima.utils.formatNameFromEmail
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class FieldAgentDashboardFragment : Fragment() {

    private lateinit var binding: FragmentFieldAgentDashboardBinding
    private lateinit var userProfileTxt: TextView
    private val viewModel: MainViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null

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

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireFragmentManager().backStackEntryCount == 0) {
                    requireActivity().finishAffinity()
                } else {
                    exitDialog()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)

        userProfileTxt = view.findViewById<TextView>(R.id.userNameTextView)

        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }

        binding.includeToolBar.userNameTextView.text = arguments?.getString("fieldAgentEmail")
            ?.let { formatNameFromEmail(it) }

        binding.textViewLastLoggedInTimeDate.text = "Last seen: "+DateFormat.getCurrentDate()

        setUpUi()

        setUpRecyclerView()

        fetchAllFieldAgentCreatedAgroDealers()

        viewModel.getAgentPoints("${arguments?.getString("fieldAgentEmail")}")

        subscribeToFieldAgentEarningVMObservables()
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

        binding.apply {

            val agentEmail = arguments?.getString("fieldAgentEmail")

            button4.setOnClickListener {
                if (agentEmail != null) {
                    showAddANewAgroDealerBottomSheetFragment(agentEmail)
                }
            }

            button3.setOnClickListener {
                lifecycleScope.launch {
                    progressBar7.isVisible = true
                    delay(500L)
                    progressBar7.isVisible = false
                    // Load agent points initially
                    viewModel.getAgentPoints("${arguments?.getString("fieldAgentEmail")}")
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
        viewModel.agentPoints.observe(viewLifecycleOwner) {
            it?.let {
                binding.textViewEarnings.text = "Current Earnings: ${it.points} points worth KSH. ${it.earnings}"
            }
        }
    }
    private fun showAddANewAgroDealerBottomSheetFragment(fieldAgentEmail: String) {
        // pass here field agent ID here as an arg who registered / added this new farmer

//        val bottomSheet = AddANewAgroDealerBottomSheetFragment.newInstance(agentEmail)
//        bottomSheet.show(parentFragmentManager, bottomSheet.tag)

        val modal = AddANewAgroDealerBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString("fieldAgentEmail", fieldAgentEmail)
            }
        }
        // modal.show(parentFragmentManager, TAG)
        parentFragmentManager.let {
            modal.show(it, AddANewAgroDealerBottomSheetFragment.TAG)
        }

        Timber.tag("ShowAddAgrodealerBottomSheet").d("Passed arg is: ${arguments?.getString("fieldAgentEmail")}")

//        val fragment = AddANewAgroDealerBottomSheetFragment()
//        val bundle = Bundle().apply {
//            putString("agentEmail","${arguments?.getString("fieldAgentEmail")}")
//        }
//        fragment.arguments = bundle
//        fragment.show(parentFragmentManager, fragment.tag)

    }



    private fun searchingAgroDealer(searchText: String) {
        val filteredList = fieldAgentAgroDealers.filter { it.name.equals(searchText, ignoreCase = true) }
        fieldAgentAddedAgroDealersAdapter.submitList(filteredList.toMutableList())
    }

    private fun exitDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Exit!")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, which ->
                clearBackStack()
                findNavController().navigate(
                    R.id.fieldAgentLoginFragment
                )
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
        builder.show()
    }
    private fun clearBackStack() {
        val fragmentManager = requireActivity().supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            val first = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
    override fun onResume() {
        super.onResume()
        displaySnackBar("Yaaay! Onboard more Agro-Dealers to increase your earnings")
    }
    companion object {
        const val TAG = "AddANewAgroDealerBottomSheetFragment"


            private const val ARG_AGENT_EMAIL = "agentEmail"

            // Create new instance of BottomSheet with argument
            fun newInstance(agentEmail: String): AddANewAgroDealerBottomSheetFragment {
                val fragment = AddANewAgroDealerBottomSheetFragment()
                val args = Bundle().apply {
                    putString(ARG_AGENT_EMAIL, agentEmail)
                }
                fragment.arguments = args
                return fragment
            }
    }
}