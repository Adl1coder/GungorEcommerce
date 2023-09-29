package com.adilegungor.gungorecommerce.ui.detay

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adilegungor.gungorecommerce.common.Resource
import com.adilegungor.gungorecommerce.data.model.AddToCartRequest
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.data.repository.ProductRepository
import com.adilegungor.gungorecommerce.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository, application: Application
): BaseViewModel(application) {

    private var _productDetailState = MutableLiveData<ProductDetailState>()
    val productDetailState: LiveData<ProductDetailState>
        get() = _productDetailState

    fun getProductsDetail(id: Int) {
        launch {
            _productDetailState.value = ProductDetailState.Loading
            val result = productRepository.getProductsDetail(id)

            when (result) {
                is Resource.Success -> {
                    _productDetailState.value = ProductDetailState.Data(result.data)
                }

                is Resource.Error -> {
                    _productDetailState.value = ProductDetailState.Error(result.throwable)
                }
            }
        }
    }

    fun addProductToCart(addToCartRequest: AddToCartRequest) {
        launch {
            productRepository.addProductToCart(addToCartRequest)
        }
    }
}

sealed interface ProductDetailState {
    object Loading: ProductDetailState
    data class Data(val productResponse: ProductUI): ProductDetailState
    data class Error(val throwable: Throwable): ProductDetailState
}