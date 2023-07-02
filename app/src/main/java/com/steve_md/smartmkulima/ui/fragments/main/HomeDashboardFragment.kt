package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import java.util.*


class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding

    //private val getUserViewModel: MainViewModel by viewModels()

   // private lateinit var currentFragment: Fragment

    private val args: HomeDashboardFragmentArgs by navArgs()
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()
        binding.includeToolBar.menuIcon.setOnClickListener {
            AlertDialog.Builder(
                requireActivity()
            ).setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, which ->
//                    requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//                        .edit().clear().apply()
                    performLogout()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        return binding.root
    }

    private fun performLogout() {
        findNavController().navigate(R.id.action_homeDashboardFragment2_to_signInDetailsWithEmailFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        //greetingDateTime.text = System.currentTimeMillis().toString()
        // Get Current Time
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }
        username = args.username
        binding.includeToolBar.userNameTextView.text = username

    }

}