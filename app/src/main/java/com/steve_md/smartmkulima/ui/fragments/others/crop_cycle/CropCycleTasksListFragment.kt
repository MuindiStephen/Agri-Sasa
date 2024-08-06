package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media.app.NotificationCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.data.remote.CyclesApiClient
import com.steve_md.smartmkulima.databinding.FragmentCropCycleListBinding
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.LocalTasks
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 *
 * Hey!
 * This code looks Junk :(
 *  @author MuindiStephen - Github
 *
 *  @year 2024
 *
 *  Was written in hurry to solve an issue
 *  & to implement a feature in urge';ncy
 *
 *
 *
 *
 */
@AndroidEntryPoint
class CropCycleTasksListFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleListBinding
    private var cycleList =  mutableListOf<LocalFarmCycle>()

    private lateinit var localFarmCycleAdapter: LocalFarmCycleAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.imageViewBackFromCropCycleLists.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpBinding()
        setUpRecyclerView()
        // getAllAvailableCropCycle()

        getAllCreatedCycles()

        val sharedPreferences = requireActivity().getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

        val lastNotificationDate = sharedPreferences.getString("last_notification_date", null)
        val currentDate = getCurrentDate()

        /**
         * Check if notifications have already been sent!
         */
        // Call scheduleNotification with tasks for today
        if (lastNotificationDate != currentDate) {
            // Call scheduleNotification with tasks for today
            viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
                if (!cycles.isNullOrEmpty()) {
                    val todayTasks = getTasksForToday(cycles)
                    todayTasks.forEach { (cropName, tasks) ->
                        scheduleNotification(cropName, tasks)
                    }
                    // Update the last notification date in SharedPreferences
                    sharedPreferences.edit().putString("last_notification_date", currentDate).apply()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllCreatedCycles() {
        /**viewModel.allCycles.observe(viewLifecycleOwner){ it ->
            when(it.isEmpty()) {
                true -> {
                    displaySnackBar("No created farm cycles available")
                }
                false -> {
                    it?.let {
                        cycleList.addAll(it)
                        localFarmCycleAdapter.submitList(cycleList)
                    }
                    localFarmCycleAdapter.notifyDataSetChanged()
                    binding.cropCycleRecyclerView.adapter = localFarmCycleAdapter
                    binding.cropCycleRecyclerView.visibility = View.VISIBLE

                    displaySnackBar("Alert! Created farm cycles")
                }
            }
        }
        */

        viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
            if (cycles.isNullOrEmpty()) {
                displaySnackBar("No created farm cycles available")
            } else {
                cycleList.clear()
                cycleList.addAll(cycles)
                localFarmCycleAdapter.submitList(cycleList.toList())

            }
        }
    }

    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.cropCycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter

        localFarmCycleAdapter = LocalFarmCycleAdapter(LocalFarmCycleAdapter.OnClickListener{ cycle->
            Log.e("...CreatedFarmCycles....", cycle.toString())

            Timber.i("=====Checking=======>: ${cycle.cropName} cycle")

            val directions = CropCycleTasksListFragmentDirections
                .actionCropCycleTasksListFragmentToDetailedFarmCycleFragment(cycle)
            findNavController().navigate(directions)
        })

        // Set the adapter to the RecyclerView
        binding.cropCycleRecyclerView.adapter = localFarmCycleAdapter
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpBinding() {
       // binding.imageViewBackFromCropCycleLists.setOnClickListener { findNavController().navigateUp() }

        binding.textView84.isVisible = false
        binding.textView74.setOnClickListener {
            // binding.cropCycleRecyclerView.removeAllViews()
            binding.progressBarCycles.visibility = View.VISIBLE
            android.os.Handler().postDelayed({
                binding.progressBarCycles.visibility = View.GONE
//                getAllAvailableCropCycle()
            }, 1000)
        }

        binding.textView83CropCycle.isVisible = false
        binding.textView83CropCycle.setOnClickListener {
            //binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("")
        }

        binding.textView84.isVisible = false
        binding.textView84.setOnClickListener {
            // binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("")
        }

        binding.searchProduct.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideKeyboard()

                val searchText = binding.searchView.editText?.text.toString().trim()

                if (searchText.isEmpty()) {
                    toast("Enter some text in order to search")
                    false
                }
                searching(searchText)
                true
            } else {
                false
            }

        }

        binding.searchView.setEndIconOnClickListener {
            hideKeyboard()

            if (binding.searchView.editText?.text.isNullOrEmpty()) {
                return@setEndIconOnClickListener
            }

            binding.searchView.editText?.setText("")
            getAllCreatedCycles()
        }

    }
    private fun getAllAvailableCropCycle() {
        // fetch from api(remote web service)
        CyclesApiClient.api.getAllFarmCycles()
            .enqueue(object : retrofit2.Callback<ArrayList<Cycle>> {
                @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
                override fun onResponse(
                    call: Call<ArrayList<Cycle>>,
                    response: Response<ArrayList<Cycle>>
                ) {
                    if (response.isSuccessful) {

                        Timber.i("==== Viewing Farm cycles${response.body()}=====")
                       // displaySnackBar("Viewing Available cycles")

                        val cycles = response.body()

//                        cycles?.let {
//                            cycleList.addAll(it)
//                            cycleListAdapter.submitList(cycleList)
//                        }
//                        cycleListAdapter.notifyDataSetChanged()
//                        binding.cropCycleRecyclerView.adapter = cycleListAdapter
//                        binding.cropCycleRecyclerView.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<ArrayList<Cycle>>, t: Throwable) {
                    Timber.e("nothing here.${t.localizedMessage}")
                    binding.errorNotAvailable.visibility = View.VISIBLE
                    binding.cropCycleRecyclerView.visibility = View.INVISIBLE
                }
            })
    }

    private fun searching(s: String) {
        val filteredList = cycleList.filter { it.cropName.equals(s, ignoreCase = true) }
        localFarmCycleAdapter.submitList(filteredList.toMutableList())
    }
    private fun filterCycles(s: String) {
        val filteredList = cycleList.filter { it.farmName.equals(s, ignoreCase = true) }
        localFarmCycleAdapter.submitList(filteredList.toMutableList())
    }


    /**
     * A method to fetch tasks for today from a list of crop cycles
     */
    private fun getTasksForToday(cycles: List<LocalFarmCycle>):  Map<String, List<LocalTasks>> {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        return cycles.associate { cycle ->
            val tasksForToday = cycle.tasks.filter { task ->
                task.startDate == today || task.endDate == today
            }
            cycle.cropName to tasksForToday
        }
    }

    /**
     * Notification channel to accept a list of tasks for
     * today and create notifications for them
     */
    @SuppressLint("SetTextI18n")
    private fun scheduleNotification(cropName: String,tasks: List<LocalTasks>) {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_id"
            val channelName = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableLights(true)
            channel.lightColor = resources.getColor( R.color.main1)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        if (tasks.isNotEmpty()) {
            tasks.forEach { task ->
                val notificationId = Random.nextInt()
                val intent = Intent(requireActivity(), AutoCreateCropCycleFragment::class.java)

                val pendingIntent = PendingIntent.getActivity(
                    requireActivity(),
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )



                val notificationMessage = "Today's task for ${cropName}: ${task.taskName}"

                val builder = androidx.core.app.NotificationCompat.Builder(requireContext(), "notification_id")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Task Reminder")
                    .setContentText(notificationMessage)
                    .setTicker("Task Reminder")
                    .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
                    .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                val notification = builder.build()

                notificationManager.notify(notificationId, notification)
            }

            displaySnackBar("You have tasks scheduled for today!")
        } else {
            displaySnackBar("No tasks scheduled for today.")
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }


}
