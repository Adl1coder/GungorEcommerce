package com.adilegungor.gungorecommerce.ui.sepet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adilegungor.gungorecommerce.common.Resource
import com.adilegungor.gungorecommerce.data.model.ClearCartRequest
import com.adilegungor.gungorecommerce.data.model.DeleteFromCartRequest
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.data.repository.ProductRepository
import com.adilegungor.gungorecommerce.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository, application: Application
) : BaseViewModel(application) {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState

    private var _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double>
        get() = _totalAmount




    fun getCartProducts(userId: String) {
        launch {
            _cartState.value = CartState.Loading
            when (val result = productRepository.getCartProduct(userId)) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data(result.data)
                    _totalAmount.value = result.data.sumOf { it.price }
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }
    }

    fun deleteProduct(request: DeleteFromCartRequest, price: Double) {
        launch {
            productRepository.deleteProductFromCart(request)
            _totalAmount.value = _totalAmount.value?.minus(price)
        }
    }

    fun clearProduct(request: ClearCartRequest, userId: String) {
        launch {
            when (val result = productRepository.clearProductFromCart(request)) {
                is Resource.Success -> {
                    getCartProducts(userId)
                    _totalAmount.value = 0.0
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }
    }

    fun onIncreaseClick(price: Double) {
        _totalAmount.value = _totalAmount.value?.plus(price)
    }

    fun onDecreaseClick(price: Double) {
        _totalAmount.value = _totalAmount.value?.minus(price)
    }
}

sealed interface CartState {
    object Loading : CartState
    data class Data(val products: List<ProductUI>) : CartState
    data class Error(val throwable: Throwable) : CartState
}
