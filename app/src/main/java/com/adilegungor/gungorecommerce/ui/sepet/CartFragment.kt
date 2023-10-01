package com.adilegungor.gungorecommerce.ui.sepet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.gone
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.common.visible
import com.adilegungor.gungorecommerce.data.model.ClearCartRequest
import com.adilegungor.gungorecommerce.data.model.DeleteFromCartRequest
import com.adilegungor.gungorecommerce.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartProductsAdapter.CartProductListener {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var cartProductsAdapter: CartProductsAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var userId: String

    private var totalAmount = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        userId = auth.currentUser!!.uid
        val request = ClearCartRequest(userId)

        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        cartProductsAdapter = CartProductsAdapter(this, sharedPreferences)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = CartFragmentDirections.actionCartFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.getCartProducts(userId)

        with (binding) {
            rvCartProducts.adapter = cartProductsAdapter

            btnClear.setOnClickListener {
                viewModel.clearProduct(request,userId)

                totalAmount = 0.0

                val editor = sharedPreferences.edit()
                for (product in cartProductsAdapter.currentList) {
                    editor.putInt("count_key_${product.id}", 0)
                }
                editor.apply()
            }

            btnBuy.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToPaymentFragment())
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Loading -> {
                    progressBar.visible()
                }

                is CartState.Data -> {
                    cartProductsAdapter.submitList(state.products)
                    rvCartProducts.isVisible = state.products.isNotEmpty()
                    progressBar.gone()

                    if (state.products.isEmpty()) {
                        rvCartProducts.visibility = View.GONE
                        tvTotal.visibility = View.GONE
                        tvAmount.visibility = View.GONE
                        btnClear.visibility = View.GONE
                        btnBuy.visibility = View.GONE

                        tvError.visibility = View.VISIBLE
                        tvError.setText("Sepetinizde hiç ürün yok!")
                    } else {
                        rvCartProducts.visibility = View.VISIBLE
                        tvTotal.visibility = View.VISIBLE
                        tvAmount.visibility = View.VISIBLE
                        btnClear.visibility = View.VISIBLE
                        btnBuy.visibility = View.VISIBLE
                        tvError.visibility = View.GONE

                    }

                }

                is CartState.Error -> {
                    progressBar.gone()
                }
            }
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) {
            tvTotal.text = "${it} ₺"
        }
    }

    override fun onProductClick(id: Int) {
        val action = CartFragmentDirections.actionCartFragmentToProductDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(id: Int, price: Double) {
        val request = DeleteFromCartRequest(id)
        viewModel.deleteProduct(request, price)
        viewModel.getCartProducts(userId)
    }

    override fun onIncreaseClick(price: Double) {
        viewModel.onIncreaseClick(price)
    }

    override fun onDecreaseClick(price: Double) {
        viewModel.onDecreaseClick(price)
    }
}