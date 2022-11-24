package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import timber.log.Timber


class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding

    private val getUserViewModel : MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        var getUserViewModel =
//            ViewModelProvider(
//                requireActivity(),
//                defaultViewModelProviderF actory
//            )[MainViewModel::class.java]

        getUserViewModel.getUser()


        getUserViewModel.user.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    Log.d("User Profile", "profile: ${it.value.userName}")
                    binding.includeToolBar.textViewUserNameProfile.text = it.value.userName
                }
                is Resource.Loading -> {
                    toast("Loading")
                }
                is Resource.Error -> {
                    toast("Error updating your profile")
                    toast("${it.errorCode}")
                }
            }
        })

        // Get user
//        lifecycleScope.launchWhenResumed {
//            getUserViewModel.userValue.collectLatest {
//                when(it) {
//                    is Resource.Success -> {
//                        updateUserProfile(it.value)
//                        Log.d("$it.value.userName", " Good morning!")
//                    }
//                    is Resource.Loading -> {
//
//                    }
//                    else -> {}
//                }
//            }
//        }

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