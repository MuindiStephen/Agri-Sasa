package com.steve_md.smartmkulima.ui.fragments.others

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Save the state when switch is enabled.
         */
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Authentication", 0)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("TEXT", "1")
        editor.putBoolean("FINGERPRINT_LOCK", binding.switch1FingerPrintLock.isChecked)
        editor.apply()


    }
}