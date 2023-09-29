package com.adilegungor.gungorecommerce.ui.giris

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.databinding.FragmentSignInBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        with(binding) {
            tvDontHaveAnAccount.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)
                } else {
                    Snackbar.make(requireView(), "Boş alanları doldurunuz.", 1000).show()
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.signInFragmentToHomeFragment)
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