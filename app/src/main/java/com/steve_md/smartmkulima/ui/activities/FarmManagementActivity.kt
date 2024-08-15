package com.steve_md.smartmkulima.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.tabadapter.VPAdapter
import com.steve_md.smartmkulima.databinding.ActivityFarmManagementBinding
import com.steve_md.smartmkulima.utils.customutilclasses.CustomTabBar


/**
 * @author - Stephen Muindi
 * @year - 2024, 15th August
 *
 * Will wrap  (+Add farm Cycle and View created / available farm cycle via a tabs layout )
 */
class FarmManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFarmManagementBinding
    private val vpAdapter = VPAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmManagementBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Hide support fragment
        supportActionBar?.hide()

        // Attach the CustomBarLayout with Viewpager2
        val viewPager = findViewById<ViewPager2>(R.id.vp)
        viewPager.adapter = vpAdapter
        val tabBar = findViewById<CustomTabBar>(R.id.tab_bar)
        tabBar.attachTo(viewPager)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)
    }
}