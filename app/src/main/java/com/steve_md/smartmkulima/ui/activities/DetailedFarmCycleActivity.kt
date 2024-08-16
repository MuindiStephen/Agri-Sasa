package com.steve_md.smartmkulima.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.ActivityDetailedFarmCycleBinding
import com.steve_md.smartmkulima.model.LocalFarmCycle

class DetailedFarmCycleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedFarmCycleBinding
    private val tasksAdapter by lazy { LocalFarmCycleTasksAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedFarmCycleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root) // Use the binding root instead of R.layout.activity_detailed_farm_cycle

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        // Retrieve the LocalFarmCycle object from the intent
        val localFarmCycle: LocalFarmCycle? = intent.getParcelableExtra("localFarmCycle")

        binding.apply {
            // Link data with respective views
            ShowCropName.text = "Crop: ${localFarmCycle?.cropName?.replace("-", "-\n")}"
            ShowStartDate.text = "Start day: ${localFarmCycle?.startDate}"
            textView78.text = "."

            // Check if localFarmCycle is not null and update the adapter with tasks
            localFarmCycle?.let {
                tasksAdapter.submitList(it.tasks)
            }

            // Set up the RecyclerView
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@DetailedFarmCycleActivity)
                adapter = tasksAdapter
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
