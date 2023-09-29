package com.adilegungor.gungorecommerce

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
        }

        setContentView(binding.root)
    }
}