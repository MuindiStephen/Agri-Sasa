package com.steve_md.smartmkulima.ui.fragments.main
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentNotificationsBinding
import timber.log.Timber
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpBinding()

        // Example: Schedule a notification for 2 days later
        scheduleNotification(2)

    }


    private fun setUpBinding() {
        binding.imageViewBackFromInboxMsgs.setOnClickListener { findNavController().navigateUp() }
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
            binding.textViewNotificationsAvailable.visibility = View.VISIBLE
            binding.textView71.visibility = View.GONE
            binding.alerts.visibility = View.GONE
            binding.textViewNotificationsAvailable.text = "Success! Please check your background for available notifications\n\n" +
                    "Crop cycle tasks upcoming in $daysLater days\n"
            Timber.v("Notify success: $notification")
        }
        else{
            binding.textViewNotificationsAvailable.visibility = View.GONE
            binding.textView71.visibility = View.VISIBLE
            binding.alerts.visibility = View.VISIBLE
        }
    }
}
