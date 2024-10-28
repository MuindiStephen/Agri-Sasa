package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentYourAccountBinding
import com.steve_md.smartmkulima.ui.fragments.others.Settings
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.getAppVersionName
import com.steve_md.smartmkulima.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class YourAccountFragment : Fragment() {

    private var _binding: FragmentYourAccountBinding? = null
    private val binding get() = _binding!!

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourAccountBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setAppPFSVersionName()

        initBinding()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun setAppPFSVersionName() {
        try {
            binding.textViewPFSAppVersion.text = "(PFS) Version: "+getAppVersionName(requireContext())
        }catch (e : Throwable){
            Timber.i("PFS Version",e.message ?: "Version Error")
        }
    }

    private fun initBinding() {

        binding.apply {

            cardViewProfileDetails.setOnClickListener {
                findNavController().navigate(R.id.profileDetailsAccountFragment)
            }

            cardViewSettings.setOnClickListener {
                // startActivity(Intent(requireActivity().applicationContext, Settings::class.java))
                // requireActivity().finish()

                findNavController().navigate(R.id.settingsFragment)
            }

            // SignOut and pop the
            cardViewLogout.setOnClickListener {
                logoutDialog()
            }
            cardViewShare.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT,"Agri-Sasa App | Hello, I found this useful AgriTech app that digitizes and automates farming processes")
                startActivity(Intent.createChooser(intent,"Share via :)"))
            }
            cardViewHistory.setOnClickListener {
                 findNavController().navigate(R.id.action_yourAccountFragment_to_transactionsHistory2)
            }

            imageViewBackFromAccount.setOnClickListener {
                findNavController().navigateUp()
            }

            cardViewRateTheApp.setOnClickListener {
                try {
                    val playStoreUrl = "https://play.google.com/store/apps/details?id=com.steve_md.smartmkulima&hl=en-US"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl)))
                    Timber.d("GOOGLE-PLAY=>Rate_App_PlayStore Request Success")
                } catch (e: ActivityNotFoundException) {
                    Timber.d("GOOGLE-PLAY=>Rate_App_PlayStore ERROR ${e.message}")
                    toast("Failed to open Google Play")
                }
            }
        }
    }

    private fun logoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Log Out & Exit App")
            .setMessage("Do you want to logout & exit?")
            .setPositiveButton("Yes") { dialog, which ->

                if (firebaseAuth?.uid != null) {
                    firebaseAuth!!.signOut()
                    Timber.d("Session Ended. for ${firebaseAuth!!.currentUser?.email}" +
                            "\n Logout Successful")
                } else {
                    Timber.d("No session found.\n" +
                            "But user logged out successfully.")
                }

                val fragmentManager = requireActivity().supportFragmentManager
                if (fragmentManager.backStackEntryCount > 0) {
                    val first = fragmentManager.getBackStackEntryAt(0)
                    fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }

                requireActivity().finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    companion object {
        const val TAG = "YourAccountProfileFragment"
    }
}