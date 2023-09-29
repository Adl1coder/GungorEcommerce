package com.adilegungor.gungorecommerce.ui.kayit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.FragmentSignUpBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        auth.currentUser?.let {
            findNavController().navigate(R.id.signUpFragmenttohomeFragment)
        }

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        with(binding) {
            btnSignup.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signUp(email, password)
                } else {
                    Snackbar.make(requireView(), "Boşlukları doldurunuz", 1000).show()
                }
            }

            tvHaveAnAccount.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.signUpFragmenttohomeFragment)
                }
                .addOnFailureListener {
                    Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
                }
        } else {
            Snackbar.make(requireView(), "Geçersiz mail veya şifre", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}