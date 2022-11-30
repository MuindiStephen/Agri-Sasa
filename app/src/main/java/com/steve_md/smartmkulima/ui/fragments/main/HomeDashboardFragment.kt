package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel


class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding

    private val getUserViewModel : MainViewModel by viewModels()

    private lateinit var currentFragment: Fragment

    private val args:HomeDashboardFragmentArgs by navArgs()
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)


        binding.includeToolBar.menuIcon.setOnClickListener{
            AlertDialog.Builder(
                requireActivity()
            ) .setTitle("Logout")
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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homeDashboardFragment -> {
                  currentFragment = HomeDashboardFragment()
                }
                R.id.chatFragment -> {
                    // Whatsapp chat with expert
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello Expert at Shamba App Company.")
                    sendIntent.type = "text/plain"
                    sendIntent.setPackage("com.whatsapp")
//                        sendIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=254740495903"))
                    startActivity(Intent.createChooser(sendIntent, "Chat With Shamba App Expert"))
                    startActivity(sendIntent)
                }
            }
            true
        }


        return binding.root


    }

    private fun performLogout() {
        findNavController().navigate(R.id.action_homeDashboardFragment2_to_signInDetailsWithEmailFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = args.username
        binding.includeToolBar.userNameTextView.text = username

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
                    toast("Loading....")
                }
                is Resource.Error -> {
                    displaySnackBar("Profile Updated")
//                    toast("${it.errorCode}")
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

        

//        val menuIcon:ImageView = view.findViewById(R.id.menuIcon)


    }




}