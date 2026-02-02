package com.steve_md.smartmkulima.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Launcher screen of this APP.
 */
@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(
            layoutInflater,
            container,

            false
        )

        binding.composeView1.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {

                Image(
                    modifier = Modifier.fillMaxWidth(),
                    //.size(),
                    painter = painterResource(
                        R.drawable.ic_plant_growing_svgrepo_com
                    ),
                    contentDescription = "splash screen logo"
                )
            }
        }

        val noteWorthBoldFont =  FontFamily(
                Font(
                resId = R.font.noteworthy_bold,
                weight = FontWeight.Bold
            )
        )

        binding.composeView3.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = noteWorthBoldFont,
                    textAlign = TextAlign.Center,
                    text = "AGRI SASA"
                    //style = MaterialTheme.typography.bodyLarge
                )
            }
        }


        val montSerratSemiBoldFont =  FontFamily(
            Font(
                resId = R.font.montserrat_semibold,
                weight = FontWeight.SemiBold
            )
        )

        binding.composeView2.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = montSerratSemiBoldFont,
                        textAlign = TextAlign.Center,
                        text = "@ 2026",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        performNavigation()
    }

    private fun performNavigation() {
        lifecycleScope.launch {

            delay(1500L)
            val userIsLoggedIn = FirebaseAuth.getInstance().currentUser
            if (userIsLoggedIn != null) {
                findNavController().navigate(
                    R.id.action_splashFragment_to_homeDashboardFragment2)
            } else {
                findNavController().navigate(
                    R.id.action_splashFragment_to_userTypeAccountFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}