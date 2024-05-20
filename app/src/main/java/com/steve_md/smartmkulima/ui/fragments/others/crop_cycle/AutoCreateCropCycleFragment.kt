package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAutoCreateCropCycleBinding
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random


class AutoCreateCropCycleFragment : Fragment() {

    private lateinit var binding: FragmentAutoCreateCropCycleBinding
    private var cropCycleStartDay: Calendar? = null


    private val cycleTypes by lazy { resources.getStringArray(R.array.cycle_types) }

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

        cropCycleStartDay = Calendar.getInstance()
        setUpBinding()

        scheduleNotification(2)
    }

    @SuppressLint("SetTextI18n")
    private fun scheduleNotification(daysLater: Int) {
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

        val notificationId = Random().nextInt()

        val builder = NotificationCompat.Builder(requireContext(), "notification_id")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Crop cycle tasks")
            .setContentText("Your have tasks due in $daysLater days.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notification = builder.build()

        // Schedule the notification
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        //calendar.set(Calendar.HOUR_OF_DAY,9)

        //for reminding of upcoming tasks
        calendar.add(Calendar.DATE,daysLater)

        val intervalMillis = 60 * 60 * 1000 // Interval: 1 hour

        var notifySuccess = true

        for (i in 0 until 24) {
            // 24 reminders in a day
            try {
                notificationManager.notify(notificationId + i, notification)
                calendar.add(Calendar.HOUR_OF_DAY, intervalMillis)
            } catch (e: Exception){
                notifySuccess = false
                break
            }
        }
        notificationManager.notify(notificationId, notification)
        Timber.tag(this.tag.toString()).d("unread msg: task notification available")

        if (notifySuccess){

            displaySnackBar("Crop cycle tasks upcoming in $daysLater days")
//            binding.textViewNotificationsAvailable.visibility = View.VISIBLE
//            binding.textView71.visibility = View.GONE
//            binding.alerts.visibility = View.GONE
//            binding.textViewNotificationsAvailable.text = "Success! Please check your background for available notifications\n\n" +
//                    "Crop cycle tasks upcoming in $daysLater days\n"
            Timber.v("Notify success: $notification")
        }
        else{
            displaySnackBar("You will be reminded of upcoming tasks later today!")

//            binding.textViewNotificationsAvailable.visibility = View.GONE
//            binding.textView71.visibility = View.VISIBLE
//            binding.alerts.visibility = View.VISIBLE
        }
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
                        stepTextView.text = "$cropCycleStartDay | ${binding.enterFarmBlockID.text}"

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
                //displayServiceCycleTasks()

//                val stepTextView = TextView(requireContext())
//                stepTextView.text = "SERVNext Heat -> Drying -> Steaming -> Calving"
//                binding.stepLinearLayout.addView(stepTextView)

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
        val startDayForCropCycle = binding.cropCycleStartDay.text.toString()
        val farmOrBlockId = binding.enterFarmBlockID.text.toString()

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
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.cropCycleStartDay.setText(dateFormat.format(cropCycleStartDay!!.time))
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
}