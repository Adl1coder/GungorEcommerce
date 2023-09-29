package com.adilegungor.gungorecommerce.ui.splash

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.databinding.FragmentSplashBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        //Status Bar
        val window = requireActivity().window

        // Before Android 6.0 Marshmallow
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Status bar will be white
            window.statusBarColor = Color.WHITE
        } else {
            if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                // Light mode
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            } else {
                // Dark mode
                window.decorView.systemUiVisibility = 0
                window.statusBarColor = Color.BLACK
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                // If user is logged in, redirect to Home screen.
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                // If the user is not logged in, redirect them to the SignIn screen.
                findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, SPLASH_TIME_OUT)
    }

    companion object {
        private const val SPLASH_TIME_OUT: Long = 3000
    }
}