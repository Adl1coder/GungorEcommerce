package com.adilegungor.gungorecommerce.ui.odeme

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.FragmentResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        val gifImg = pl.droidsonroids.gif.GifDrawable(resources, R.drawable.cargo)

        with(binding) {
            ivCargo.setImageDrawable(gifImg)

            btnHome.setOnClickListener {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHomeFragment())
            }
        }
    }
}