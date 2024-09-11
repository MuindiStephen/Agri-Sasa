package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.data.remote.GapApiClient
import com.steve_md.smartmkulima.databinding.FragmentAutoCreateCropCycleBinding
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.model.GAPtask
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.LocalTasks
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.utils.DateFormat.getWhenStarts
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import java.util.Random


/**
 * Creating Farm cycle  for both Crops and Livestock
 *
 * This also automates cycle creation
 *
 * Notify on upcoming tasks of different farm cycles
 */

@AndroidEntryPoint
class AutoCreateCropCycleFragment : Fragment() {

    private lateinit var binding: FragmentAutoCreateCropCycleBinding
    private var cropCycleStartDay: Calendar? = null

    private var selectedCrop: String = ""
    private var selectedfarm: String = ""
    private val cycleTypes by lazy { resources.getStringArray(R.array.cycle_types) }

    private val farmCycleViewModel: MainViewModel by viewModels()

    private var farmFieldsList =  mutableListOf<NewFarmField>()

    var task : LocalTasks? = LocalTasks("","","")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAutoCreateCropCycleBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        fetchAvailableFarms()

        binding.imageViewBackFromAutoCreateCropCycle.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewAllCycleTypes.setOnClickListener {
            goToCropCycle()
        }

        cropCycleStartDay = Calendar.getInstance()
       // setUpBinding()

       // scheduleNotification(2)


        // Adapter to attach Spinner with data
        val cropList = resources.getStringArray(R.array.crop_list)
        val cropAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cropList)
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCrops.adapter = cropAdapter

        binding.spinnerCrops.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCrop = parent.getItemAtPosition(position).toString()
                Timber.i("Selected Crop: $selectedCrop")
                displaySnackBar(selectedCrop)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
               displaySnackBar("Nothing selected")
               Timber.tag("AutoCreateCropCycle").d("No CROP FOUND")
            }
        }

        // Setting up Img and PopUpMenu
        binding.imageView21.setOnClickListener {

            binding.imageView21
                .animate()
                .rotation(180f)
                .setDuration(200)
                .withEndAction {

                    val popUpMenu = androidx.appcompat.widget.PopupMenu(requireContext(), it)

                    cropList.forEach { string ->
                        popUpMenu.menu.add(string)
                    }

                    popUpMenu.setOnMenuItemClickListener { menuItem ->

                        val selectedCrop = menuItem.title.toString()
                        Timber.i("Selected Crop from dropdown imgView: $selectedCrop")
                        displaySnackBar(selectedCrop)
                        // Optionally, you can also update the Spinner's selection to match the selected item
                        binding.spinnerCrops.setSelection(cropList.indexOf(selectedCrop))
                        true
                    }
                    popUpMenu.show()

                    // After the PopupMenu is shown, reset the ImageView's rotation back to the initial position
                    binding.imageView21
                        .animate()
                        .rotation(0f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }

        // Fetch first GAPs which are linked to Creation of crop cycles with exact date and time
        binding.buttonGeneratorCropCycle.setOnClickListener {
            getGoodAgriculturalPractices(selectedCrop)
        }

        configureUi()
    }

    private fun configureUi() {
        binding.inputCropCycleStartDay.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }
    }


    private fun getGoodAgriculturalPractices(selectedCrop: String) {
        GapApiClient.api.getAllGAPs()
            .enqueue(object : retrofit2.Callback<ArrayList<GAP>> {
                @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
                override fun onResponse(
                    call: Call<ArrayList<GAP>>,
                    response: Response<ArrayList<GAP>>
                ) {
                    if (response.isSuccessful) {

                        val gapList = response.body()
                        if (gapList != null) {
                            // Filter GAPs based on the selected crop
                            val gapForSelectedCrop =
                                gapList.find { it.nameGAP.equals(selectedCrop, ignoreCase = true) }

                            if (gapForSelectedCrop != null) {

                                createCropCycle(
                                    selectedCrop,
                                    binding.inputFarmBlockID.selectedItem.toString(),
                                    cropCycleStartDay,
                                    gapForSelectedCrop.gap
                                )
                            }
                    }

                    } else {
                        Timber.e("Response was not successful: ${response.message()}")
                        Timber.w("No GAP found for the selected crop: $selectedCrop")
                        requireActivity().runOnUiThread {
                            displaySnackBar("No GAP found for the selected crop: $selectedCrop")
                        }
                    }
                }
                override fun onFailure(call: Call<ArrayList<GAP>>, t: Throwable) {
                    Timber.e("nothing here.${t.localizedMessage}")
                }
            })
    }

    private fun createCropCycle(
        selectedCrop: String,
        farmName: String,
        startDate: Calendar?,
        gap: List<GAPtask>
    ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = startDate ?: Calendar.getInstance()

        val localTasksList = mutableListOf<LocalTasks>()

        gap.forEach { gapTask ->

            val startDateInt = gapTask.startDate.toInt()
            val endDateInt = gapTask.endDate.toInt()

            val taskStartDate = calendar.clone() as Calendar
            taskStartDate.add(Calendar.DAY_OF_YEAR, startDateInt - 1)

            val taskEndDate = calendar.clone() as Calendar
            taskEndDate.add(Calendar.DAY_OF_YEAR, endDateInt - 1)

            task = LocalTasks(
                taskName = gapTask.taskName,
                startDate = dateFormat.format(taskStartDate.time),
                endDate = dateFormat.format(taskEndDate.time)
            )
            localTasksList.add(task!!)
           // calendar.add(Calendar.DATE, gapTask.endDate.toInt())
        }

        val dateFormat1 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar1 = startDate ?: Calendar.getInstance()

        val testStartDate = calendar1.clone() as Calendar

        val localFarmCycle = LocalFarmCycle(
            farmName = farmName,
            cropName = selectedCrop,
            startDate = getWhenStarts() ?: "${startDate?.time}",
            tasks = localTasksList
        )

        lifecycleScope.launch {
            try {
                farmCycleViewModel.addCropCycle(localFarmCycle)
                Log.d("AutoCreateCropCycleFragment","Added crop cycle: $localFarmCycle")

                requireActivity().runOnUiThread {
                    displaySnackBar("Crop cycle for $selectedCrop created successfully!")
                }

                // Schedule notifications for tasks
                scheduleNotification(localTasksList)

            } catch (e: Exception) {
                Log.e("AutoCreateCropCycleFragment", "Error adding crop cycle: ${e.message}")
                displaySnackBar("Failed to create Farm Cycle: ${e.message}")
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun scheduleNotification(tasks: List<LocalTasks>) {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_id"
            val channelName = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        val todayTasks = getTasksForToday(tasks)

        if (todayTasks.isNotEmpty()) {
            todayTasks.forEach { task ->
                val notificationId = Random().nextInt()
                val intent = Intent(requireActivity(), AutoCreateCropCycleFragment::class.java)

                val pendingIntent = PendingIntent.getActivity(
                    requireActivity(),
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val builder = NotificationCompat.Builder(requireContext(), "notification_id")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Task Reminder")
                    .setContentText("Today's task for $selectedCrop:${task.taskName}")
                    .setTicker("Task Reminder")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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

    private fun getTasksForToday(tasks: List<LocalTasks>): List<LocalTasks>  {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val todayDate = dateFormat.format(Calendar.getInstance().time)
        return tasks.filter { it.startDate == todayDate }
    }


    @SuppressLint("ResourceType", "SetTextI18n")
    private fun setUpBinding() {

        binding.viewAllCycleTypes.setOnClickListener {
            goToCropCycle()
        }

        val cycleTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cycleTypes)
        binding.spinnerCycleType.adapter = cycleTypeAdapter

        binding.spinnerCycleType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedCycleType = cycleTypes[position]
                    if (selectedCycleType == "Crop Cycle") {
                        populateCropSpinner()
                    } else if (selectedCycleType == "Service Cycle") {
                        binding.spinnerCrop.visibility = View.GONE
                        //displayServiceCycleTasks()
                        binding.stepLinearLayout.removeAllViews()
                        val stepTextView = TextView(requireContext())
                        stepTextView.text = "$cropCycleStartDay | ${binding.inputFarmBlockID.selectedItem.toString()}"

                    } else {
                        return
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.cropCycleStartDay.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        binding.buttonGeneratorCropCycle.setOnClickListener {
            val selectedCropCycle = binding.spinnerCycleType.selectedItem.toString()

            if (selectedCropCycle == "Crop Cycle") {
                generateCropCycle()
            } else {
                displayPredefinedServiceCycle()
            }
        }

    }

    private fun goToCropCycle() {
        findNavController().navigate(
            R.id.action_autoCreateCropCycleFragment_to_cropCycleTasksListFragment
        )
    }

    @SuppressLint("SetTextI18n")
    private fun displayPredefinedServiceCycle() {
        binding.stepLinearLayout.removeAllViews()
        val steps = listOf(
            Step("Date of Service", 1, 1),
            Step("Next Heat", 21, 21),
            Step("Date of Drying", 219, 219),
            Step("Date of Steaming", 249, 249),
            Step("Expected calving dates", 279, 279)
        )

        steps.forEach { step ->
            val stepTextView = TextView(requireContext())
            // Set text with step name and details
            stepTextView.text =
                "${step.name}: Start Day ${cropCycleStartDay?.time}, End Day ${step.endDay} \n"
            binding.stepLinearLayout.addView(stepTextView)
        }
    }

    data class Step(val name: String, val startDay: Int, val endDay: Int)



    private fun generateCropCycle() {
        val selectedCycleType = binding.spinnerCycleType.selectedItem.toString()
        val startDayForCropCycle = binding.inputCropCycleStartDay.text.toString()
        val farmOrBlockId = binding.inputFarmBlockID.selectedItem.toString()

        if (selectedCycleType.isNotEmpty() && startDayForCropCycle.isNotEmpty() && farmOrBlockId.isNotEmpty()) {
            // Parse start date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDay = dateFormat.parse(startDayForCropCycle)


            // Sample crop cycle stages
            val stages = listOf(
                "Seedling Acquisition",
                "Media Acquisition",
                "Media washing and Treatment",
                "Preparation of grow trays (washing and sterilizing)/potting of media",
                "Loading of treated media and seedlings",
                "Seed sowing",
                "Watering and fertilisation",
                "Hardening",
                "Land preparation",
                "Manure application",
                "Bed making/gypsum application",
                "Flushing with plain water"
            )


            binding.stepLinearLayout.removeAllViews()

            for (stage in stages) {
                val stepTextView = TextView(requireContext())
                val stageDescription = getStageDescription(stage)
                stepTextView.text = "$stage\n$stageDescription"
                stepTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main1
                    )
                )
                stepTextView.textSize = 16f
                stepTextView.setPadding(16, 16, 16, 16)

                stepTextView.setOnClickListener {
                }
                binding.stepLinearLayout.addView(stepTextView)
            }

        }

    }

    private fun populateCropSpinner() {
        val cropList = resources.getStringArray(R.array.crop_list)
        val cropAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cropList)
        binding.spinnerCrop.adapter = cropAdapter
        binding.spinnerCrop.visibility = View.VISIBLE
    }

    private fun showDatePickerDialog() {
        val calendar = cropCycleStartDay ?: Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selected date in the UI
                cropCycleStartDay?.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.inputCropCycleStartDay.setText(dateFormat.format(cropCycleStartDay!!.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun getStageDescription(stageName: String): String {
        return when (stageName) {
            "Seedling Acquisition" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-10"
            "Media Acquisition" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-10"
            "Media washing and Treatment" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-11"
            "Preparation of grow trays (washing and sterilizing)/potting of media" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-12"
            "Loading of treated media and seedlings" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-14"
            "Seed sowing" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-03-15"
            "Watering and fertilisation" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Hardening" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Land preparation" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Manure application" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-27"
            "Bed making/gypsum application" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-23"
            "Flushing with plain water" -> "Start Day: ${cropCycleStartDay!!.time} - End Day: 2024-04-24"
            else -> ""
        }
    }

    private fun fetchAvailableFarms() {
        farmCycleViewModel.allFarmFields.observe(viewLifecycleOwner) { farmField->
            if (!farmField.isNullOrEmpty()) {
                farmFieldsList.clear()
                farmFieldsList.addAll(farmField)

                val farmNames = farmFieldsList.map {
                    it.farmName
                }

                val adapter = ArrayAdapter<String>(this@AutoCreateCropCycleFragment.requireContext(),
                    android.R.layout.simple_spinner_item,farmNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.inputFarmBlockID.adapter = adapter


                binding.inputFarmBlockID.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        selectedfarm = parent.getItemAtPosition(position).toString()
                        Timber.i("Selected Farm name: $selectedfarm")
                        displaySnackBar(selectedfarm)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        Timber.tag("AutoCreateCropCycle").d("No Farm FOUND")
                        displaySnackBar("No farm found, Please create a new farm in order to continue")
                    }
                }

                /**
                 * Setting up img and pop up menu
                 */
                binding.imageViewSelectFarm.setOnClickListener {
                    binding.imageViewSelectFarm
                        .animate()
                        .rotation(180f)
                        .setDuration(200)
                        .withEndAction {

                            val popUpMenu = androidx.appcompat.widget.PopupMenu(requireContext(), it)

                            farmNames.forEach { string ->
                                popUpMenu.menu.add(string)
                            }

                            popUpMenu.setOnMenuItemClickListener { menuItem ->
                                val farmblocks = menuItem.title.toString()
                                Timber.i("Selected farm from dropdown imgView: $farmblocks")
                                displaySnackBar(farmblocks)

                                // Optionally, you can also update the EditTextInputLy selection to match the selected item

                                binding.inputFarmBlockID.setSelection(farmNames.indexOf(farmblocks))

                                val farmIndex = farmNames.indexOf(farmblocks)
                                if (farmIndex >= 0) {
                                    binding.inputFarmBlockID.setSelection(farmIndex)
                                } else {
                                    binding.inputFarmBlockID.setSelection(0)
                                }

                                true

                            }
                            popUpMenu.show()

                            // After the PopupMenu is shown, reset the ImageView's rotation back to the initial position
                            binding.imageViewSelectFarm
                                .animate()
                                .rotation(0f)
                                .setDuration(200)
                                .start()
                        }
                        .start()
                }


            } else {
                Timber.e("No farm blocks available for selection.")
                binding.buttonGeneratorCropCycle.isEnabled = false

                displaySnackBar("No farm found, please create a new farm in order to continue.")
            }
        }
    }



}



