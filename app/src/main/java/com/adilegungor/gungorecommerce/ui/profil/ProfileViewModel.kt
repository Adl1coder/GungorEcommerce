package com.adilegungor.gungorecommerce.ui.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adilegungor.gungorecommerce.R

class ProfileViewModel : ViewModel() {

    // LiveData for avatar resource ID
    private val _avatarResource = MutableLiveData<Int>()
    val avatarResource: LiveData<Int>
        get() = _avatarResource

    // LiveData for selected gender
    private val _selectedGender = MutableLiveData<String>()
    val selectedGender: LiveData<String>
        get() = _selectedGender

    // Initialize ViewModel with default values
    init {
        // Initialize LiveData with default values here
        _avatarResource.value = R.drawable.avatargirl
        _selectedGender.value = "girl"
    }

    // Update avatar resource and selected gender
    fun updateAvatar(avatarResource: Int, gender: String) {
        _avatarResource.value = avatarResource
        _selectedGender.value = gender
    }
}
