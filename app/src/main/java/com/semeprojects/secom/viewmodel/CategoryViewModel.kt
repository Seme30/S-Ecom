package com.semeprojects.secom.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semeprojects.secom.data.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _productUiState = MutableStateFlow<ProductUIState>(ProductUIState.Loading)
    val productUIState: StateFlow<ProductUIState> = _productUiState.asStateFlow()

    val category = savedStateHandle.get<String>("category")

    init {
        getProductsByCategory()
    }

    private fun getProductsByCategory(){
        viewModelScope.launch {
            try{
                val result = category?.let { productRepository.getProductsByCategory(it.lowercase()) }
                val categories = productRepository.getProductCategories()
                if (result != null) {
                    if (result.isSuccess) {
                        _productUiState.value = ProductUIState.Success(
                            ProductUiSuccessState(
                                products = MutableStateFlow(result.getOrNull() ?: emptyList()),
                                categories = MutableStateFlow(categories.getOrNull()?: emptyList())
                            )
                        )
                    } else {
                        _productUiState.value = ProductUIState.Error(
                            message = result.exceptionOrNull()?.message ?: "Error Happened $result"
                        )
                    }
                }
            } catch (e: Exception){
                _productUiState.value = ProductUIState.Error(
                    message = e.message.toString()
                )
            }
        }
    }

}