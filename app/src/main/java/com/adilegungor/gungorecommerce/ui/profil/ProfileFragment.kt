package com.adilegungor.gungorecommerce.ui.profil

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null
    private val viewModel by viewModels<ProfileViewModel>()

    private val sharedPreferencesName = "MyPreferences"
    private val keyGender = "userGender"
    private val keyAvatar = "userAvatar"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)


        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        auth = Firebase.auth

        with(binding) {

            // Verileri SharedPreferences'ten yükle
            val savedGender = sharedPreferences.getString(keyGender, null)
            val savedAvatarResource = sharedPreferences.getInt(keyAvatar, R.drawable.avatargirl)

            if (savedGender != null) {
                if (savedGender == "boy") {
                    rbBoy.isChecked = true
                } else if (savedGender == "girl") {
                    rbGirl.isChecked = true
                }

                ivProfile.setImageResource(savedAvatarResource)
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->

                when (checkedId) {
                    R.id.rb_boy -> {
                        ivProfile.setImageResource(R.drawable.avatarboy)
                        saveAvatarAndGender(R.drawable.avatarboy, "boy")
                    }
                    R.id.rb_girl -> {
                        ivProfile.setImageResource(R.drawable.avatargirl)
                        saveAvatarAndGender(R.drawable.avatargirl, "girl")
                    }
                }
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

            // Observe LiveData for selected gender
            viewModel.selectedGender.observe(viewLifecycleOwner) { gender ->
                when (gender) {
                    "boy" -> {
                        binding.rbBoy.isChecked = true
                    }

                    "girl" -> {
                        binding.rbGirl.isChecked = true
                    }
                }
            }

            // Observe LiveData for avatar resource
            viewModel.avatarResource.observe(viewLifecycleOwner) { avatarResource ->
                binding.ivProfile.setImageResource(avatarResource)
            }
        }
    }

    // Save gender to shared preferences
    fun saveGender(gender: String) {
        val editor = sharedPreferences.edit()
        editor.putString(keyGender, gender)
        editor.apply()
    }

    private fun saveAvatar(avatarResource: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(keyAvatar, avatarResource)
        editor.apply()
    }

    private fun saveAvatarAndGender(avatarResource: Int, gender: String) {
        viewModel.updateAvatar(avatarResource, gender)
        saveAvatar(avatarResource)
        saveGender(gender)
    }

}