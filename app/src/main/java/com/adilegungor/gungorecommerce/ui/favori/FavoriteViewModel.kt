package com.adilegungor.gungorecommerce.ui.favori

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adilegungor.gungorecommerce.common.Resource
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.data.repository.ProductRepository
import com.adilegungor.gungorecommerce.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val productRepository: ProductRepository, application: Application
) : BaseViewModel(application) {

    private var _favState = MutableLiveData<FavState>()
    val favState: LiveData<FavState>
        get() = _favState

    fun getFavProducts() {
        launch {
            _favState.value = FavState.Loading
            when (val result = productRepository.getFavProducts()) {
                is Resource.Success -> {
                    _favState.value = FavState.Data(result.data)
                }

                is Resource.Error -> {
                    _favState.value = FavState.Error(result.throwable)
                }
            }
        }
    }

    fun deleteProduct(product: ProductUI) {
        launch {
            productRepository.deleteProductFromFav(product)
        }
    }
}

sealed interface FavState {
    object Loading : FavState
    data class Data(val products: List<ProductUI>) : FavState
    data class Error(val throwable: Throwable) : FavState
}