package com.adilegungor.gungorecommerce.ui.anasayfa

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adilegungor.gungorecommerce.R
import com.adilegungor.gungorecommerce.common.viewBinding
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ProductAdapter.ProductListener, SalesProductAdapter.ProductListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private var bottomNavigationView: BottomNavigationView? = null
    private val productAdapter by lazy { ProductAdapter(this) }
    private val viewModel by viewModels<HomeViewModel>()
    private val salesProductAdapter by lazy { SalesProductAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.setVisibility(View.VISIBLE);
        with(binding) {
            rvAllProducts.adapter = productAdapter
            rvDiscountedProducts.adapter = salesProductAdapter
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.rb_all) {
                    viewModel.getProducts()
                } else {
                    val category = when (checkedId) {
                        R.id.rb_notebook -> "notebook"
                        R.id.rbmntr -> "monitor"
                        R.id.rbhdst->"headset"
                        R.id.rbcnsl->"console"
                        R.id.rbdsktop->"desktop"
                        else -> "all"
                    }
                    viewModel.getProductsByCategory(category)
                }
            }



            toolbar.setOnMenuItemClickListener (object : MenuItem.OnMenuItemClickListener,
                Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.action_profile -> {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
                            return true
                        }
                    }

                    return false
                }
            })
        }

        viewModel.getProducts()
        viewModel.getSaleProducts()

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> {
                    progressBar2.visibility = View.VISIBLE
                }

                is HomeState.Data -> {
                    progressBar2.visibility = View.GONE
                    productAdapter.submitList(state.productsResponse)
                }

                is HomeState.Error -> {
                    tvError.setText(state.throwable.message.orEmpty())
                    progressBar2.visibility = View.GONE
                    rvAllProducts.visibility = View.GONE
                    ivError.visibility = View.VISIBLE
                    tvError.visibility = View.VISIBLE
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }

        viewModel.salesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SalesState.Loading -> {
                    progressBar2.visibility = View.VISIBLE
                }

                is SalesState.Data -> {
                    progressBar2.visibility = View.GONE
                    salesProductAdapter.submitList(state.productsResponse)
                }

                is SalesState.Error -> {
                    progressBar2.visibility = View.GONE
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onFavoriteClick(product: ProductUI) {
        viewModel.addProductToFav(product)
        Snackbar.make(requireView(), "Favorilere Eklendi!", Snackbar.LENGTH_SHORT).show()
    }
}