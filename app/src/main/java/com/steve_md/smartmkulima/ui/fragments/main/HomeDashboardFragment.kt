package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import timber.log.Timber


class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)

        val view =   binding.root
       return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuIcon:ImageView = view.findViewById(R.id.menuIcon)

        menuIcon.setOnClickListener{
            AlertDialog.Builder(
             requireContext()
            ) .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which ->
                    performLogout()
                    requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                        .edit().clear().apply()
                    Timber.tag("Logout msg").v("Logout successfully")
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
        }
    }

    private fun performLogout() {
        findNavController().navigate(R.id.action_homeDashboardFragment3_to_signInDetailsWithEmailFragment2)
    }

}