package com.steve_md.smartmkulima.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.ActivityDetailedFarmCycleBinding
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.CropCycleCancelledStatusCommentsFragment
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.ViewCropCycleAnalyticsBottomSheetFragment
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@AndroidEntryPoint
class DetailedFarmCycleActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityDetailedFarmCycleBinding
    private val tasksAdapter by lazy { LocalFarmCycleTasksAdapter() }
  //  private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedFarmCycleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root) // Use the binding root instead of R.layout.activity_detailed_farm_cycle

        // navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // Setting Nav Controller
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment2)
//                as NavHostFragment
//        navController = navHostFragment.findNavController()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        // Support for light mode only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Retrieve the LocalFarmCycle object from the intent
        val localFarmCycle: LocalFarmCycle? = intent.getParcelableExtra("localFarmCycle")

        binding.apply {
            // Link data with respective views
            ShowCropName.text = "Crop: ${localFarmCycle?.cropName?.replace("-", "-\n")}"
            ShowStartDate.text = "Start day: ${localFarmCycle?.startDate}"
            textView78.text = "."

            val cropCycleStatus = updateCropCycleStatus(localFarmCycle!!)

            textView135.text = "Status: ${cropCycleStatus}"
            textViewComments.text = "Comments: ${localFarmCycle.comments}"

            // comments are visible only when status is cancelled.
            textViewComments.isVisible = localFarmCycle.status == "Cancelled"

            // Check if localFarmCycle is not null and update the adapter with tasks
            localFarmCycle.let {
                tasksAdapter.submitList(it.tasks)
            }

            // Set up the RecyclerView
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@DetailedFarmCycleActivity)
                adapter = tasksAdapter
            }

            // Marking the Crop Cycle As DONE
            binding.buttonMarkAsDoneCropCycle.setOnClickListener {
                showChangeCropCycleStatusDialog()
               // textViewComments.isVisible = false
            }

            binding.buttonOpenRecordsExpensesAndRevenues.setOnClickListener {
                showViewCropCycleAnalyticsBottomSheet()
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showChangeCropCycleStatusDialog() {

            val localFarmCycle: LocalFarmCycle? = intent.getParcelableExtra("localFarmCycle")

            AlertDialog.Builder(this)
                .setTitle("Change Crop Cycle status")
                .setMessage("Choose whether to cancel or mark done the crop cycle.")
                .setPositiveButton("CANCEL") { _, _ ->

                    localFarmCycle?.status = "Cancelled"

                    localFarmCycle?.let { updateCropCycleStatus(it) }

                    // Update Room DB status - field
                    if (localFarmCycle != null) {
                        viewModel.updateTaskStatus("Cancelled", localFarmCycle.cropName)
                    }

                    showLoadingState()

                    binding.textViewComments.isVisible = true

                    showCropCycleCancelledAddCommentsBottomSheetDialog()
                }
                .setNegativeButton("MARK DONE") { _, _ ->

                    localFarmCycle?.status = "Done"

                    localFarmCycle?.let { updateCropCycleStatus(it) }

                    // Update Room DB status - field
                    if (localFarmCycle != null) {
                        viewModel.updateTaskStatus("Done", localFarmCycle.cropName)
                    }

                    binding.textViewComments.isVisible = false

                    showLoadingState()
                }
                .show()
    }

    private fun showLoadingState() {
        lifecycleScope.launch {
            binding.loadingStatusUpdate.isVisible = true
            delay(3000L)
            binding.loadingStatusUpdate.isVisible = false

            this@DetailedFarmCycleActivity.runOnUiThread {
                displaySnackBar("Status updated successfully.")
            }
        }
    }

    private fun showCropCycleCancelledAddCommentsBottomSheetDialog() {
        val modal = CropCycleCancelledStatusCommentsFragment()
        supportFragmentManager.let {
            modal.show(it, CropCycleCancelledStatusCommentsFragment.TAG)
        }
    }

    private fun showViewCropCycleAnalyticsBottomSheet() {

        val localFarmCycle: LocalFarmCycle? = intent.getParcelableExtra("localFarmCycle")

        val modal = ViewCropCycleAnalyticsBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString("cropCycleName", localFarmCycle?.cropName)
            }
        }
        supportFragmentManager.let {
            modal.show(it, ViewCropCycleAnalyticsBottomSheetFragment.TAG)
        }
    }

    // Crop Cycle status
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCropCycleStatus(localFarmCycle: LocalFarmCycle): String {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val currentDate = LocalDate.now() // Get the current date
            val startDate = LocalDate.parse(localFarmCycle.startDate,formatter) // Parse the start date
            val endDate = LocalDate.parse(localFarmCycle.tasks.last().endDate,formatter2) // up to the very last crop cycle end date

            return when {
                localFarmCycle.status == "Cancelled" -> "Cancelled"
                currentDate.isBefore(startDate) -> "Upcoming"
                currentDate.isAfter(endDate) && localFarmCycle.status != "Done" && localFarmCycle.status != "Cancelled" -> "Overdue"
                currentDate.isAfter(startDate) && currentDate.isBefore(endDate) -> "In Progress"
                else -> localFarmCycle.status
            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
