package com.semeprojects.secom.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semeprojects.secom.data.network.model.Product
import com.semeprojects.secom.data.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ProductDetailUIState {
    object Loading : ProductDetailUIState()
    data class Success(val product: Product) : ProductDetailUIState()
    data class Error(val message: String) : ProductDetailUIState()
}

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _productDetailUiState = MutableStateFlow<ProductDetailUIState>(ProductDetailUIState.Loading)
    val productDetailUIState: StateFlow<ProductDetailUIState> = _productDetailUiState.asStateFlow()

    private val productId = savedStateHandle.get<String>("productId")

    init {
        getProductById()
    }

    private fun getProductById(){
        viewModelScope.launch {
            _productDetailUiState.value = ProductDetailUIState.Loading

            val result = productId?.let { productRepository.getProductById(it.toInt()) }
            if (result != null) {
                if (result.isSuccess){
                    _productDetailUiState.value = ProductDetailUIState.Success(result.getOrNull()!!)
                } else {
                    _productDetailUiState.value = ProductDetailUIState.Error(result.exceptionOrNull()?.message ?: "Error Happened $result")
                }
            }
        }
    }
}