package com.adilegungor.gungorecommerce.ui.profil

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<ProfileViewModel>()

    private val sharedPreferencesName = "UserPreferences"
    private val sharedPreferences by lazy { requireContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        auth = Firebase.auth

        with(binding) {
            val keyGender = "userGender"
            val keyAvatar = "userAvatar"

            val savedGender = sharedPreferences.getString(keyGender, null)
            val savedAvatarResource = sharedPreferences.getInt(keyAvatar, R.drawable.avatargirl)

            if (savedGender != null) {
                val genderResId = if (savedGender == "boy") R.drawable.avatarboy else R.drawable.avatargirl
                rbBoy.isChecked = savedGender == "boy"
                rbGirl.isChecked = savedGender == "girl"
                ivProfile.setImageResource(genderResId)
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val avatarResource = when (checkedId) {
                    R.id.rb_boy -> R.drawable.avatarboy
                    R.id.rb_girl -> R.drawable.avatargirl
                    else -> R.drawable.avatargirl
                }
                ivProfile.setImageResource(avatarResource)
                val gender = if (checkedId == R.id.rb_boy) "boy" else "girl"
                saveAvatarAndGender(avatarResource, gender)
            }

            tvEmail.text = auth.currentUser?.email.toString()

            tvProfileFavorites.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteFragment())
            }

            tvProfileCart.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCartFragment())
            }

            btnSignOut.setOnClickListener {
                auth.signOut()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
            }

            viewModel.selectedGender.observe(viewLifecycleOwner) { gender ->
                rbBoy.isChecked = gender == "boy"
                rbGirl.isChecked = gender == "girl"
            }

            viewModel.avatarResource.observe(viewLifecycleOwner) { avatarResource ->
                ivProfile.setImageResource(avatarResource)
            }
        }
    }

    private fun saveAvatarAndGender(avatarResource: Int, gender: String) {
        viewModel.updateAvatar(avatarResource, gender)
        sharedPreferences.edit().apply {
            putInt("userAvatar", avatarResource)
            putString("userGender", gender)
            apply()
        }
    }
}
