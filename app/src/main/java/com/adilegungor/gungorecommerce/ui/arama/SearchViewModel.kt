package com.adilegungor.gungorecommerce.ui.arama

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
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository, application: Application
) : BaseViewModel(application) {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    fun getSearchProduct(query: String) {
        launch {
            _searchState.value = SearchState.Loading
            when (val result = productRepository.getSearchProduct(query)) {
                is Resource.Success -> {
                    _searchState.value = SearchState.Data(result.data)
                }

                is Resource.Error -> {
                    _searchState.value = SearchState.Error(result.throwable)
                }
            }
        }
    }
}

sealed interface SearchState {
    object Loading : SearchState
    data class Data(val products: List<ProductUI>) : SearchState
    data class Error(val throwable: Throwable) : SearchState
}