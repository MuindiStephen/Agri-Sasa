package com.steve_md.smartmkulima.ui.activities


/**
 * Modern Agri-App
 * @author by Stephen Muindi
 * @year 2024
 */
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.ActivityMainBinding
import com.steve_md.smartmkulima.utils.SnackbarHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var screenCaptureCallback: ScreenCaptureCallback? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)





        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeDashboardFragment2 -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.applyInsuranceFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.newFarmingTechnologyFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.hireFarmEquipmentsFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                }
            }
        }
    }




}















