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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.ActivityMainBinding
import com.steve_md.smartmkulima.utils.SnackbarHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var screenCaptureCallback: ScreenCaptureCallback? = null

    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUpdate()

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

    private val updateLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if (it?.data == null) return@registerForActivityResult
        if (it.resultCode == UPDATE_REQUEST_CODE) {
               Timber.d("Download Started")
            if (it.resultCode != Activity.RESULT_OK) {
               Timber.d("Download Failed")
            }
        }
    }

    /**
     * we are getting the info if the update is available or not
     */
    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                appUpdateManager.startUpdateFlowForResult(   //  to start the app update process.
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
            }
        }
    }

    companion object {
        const val UPDATE_REQUEST_CODE = 1
    }
}















