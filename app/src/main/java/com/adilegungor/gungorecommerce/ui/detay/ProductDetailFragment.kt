package com.adilegungor.gungorecommerce.ui.detay

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.loadImage
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.data.model.AddToCartRequest
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.databinding.FragmentProductDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private val binding by viewBinding(FragmentProductDetailBinding::bind)

    private val args by navArgs<ProductDetailFragmentArgs>()

    private val viewModel by viewModels<ProductDetailViewModel>()

    private var bottomNavigationView: BottomNavigationView? = null

    private lateinit var auth: FirebaseAuth

    var product: ProductUI ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val userId = auth.currentUser!!.uid

        viewModel.getProductsDetail(args.productId)

        val request = AddToCartRequest(userId, args.productId)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.GONE);

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnAddCart.setOnClickListener {
                viewModel.addProductToCart(request)
                Snackbar.make(requireView(), "Ürün sepete eklendi!", 1000).show()
            }
        }
        observeData()
    }

    private fun observeData()  = with(binding) {

        viewModel.productDetailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ProductDetailState.Loading -> {
                   detailProgressBar.visibility = View.VISIBLE
                }

                is ProductDetailState.Data -> {
                    detailProgressBar.visibility = View.GONE
                    val scaledRating = (state.productResponse?.rate?.toFloat()!! / 5.0f) * 100.0f // Rate değeri
                    product = state.productResponse
                    if (state.productResponse != null) {
                        tvTitle.text = state.productResponse.title
                        tvDesc.text = state.productResponse.description
                        tvCategory.text = state.productResponse.category
                        ivProduct.loadImage(state.productResponse.imageOne)
                        ratingBar.rating = scaledRating / 20.0f

                        if (state.productResponse.saleState == true) {
                            tvDetailSalePrice.isVisible = true
                            tvDetailSalePrice.text = "${state.productResponse.salePrice } TL"
                            tvPrice.text = "${state.productResponse.price * state.productResponse.count} TL"
                            tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        } else {
                            tvPrice.text = "${state.productResponse.price * state.productResponse.count} TL"
                            tvDetailSalePrice.isVisible = false
                        }

                    }
                }

                is ProductDetailState.Error -> {
                    detailProgressBar.visibility = View.GONE
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}