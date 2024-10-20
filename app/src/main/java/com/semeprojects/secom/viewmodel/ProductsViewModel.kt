package com.semeprojects.secom.viewmodel

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


data class ProductUiSuccessState(
    val products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList()),
    val categories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
)

sealed class ProductUIState {
    object Loading : ProductUIState()
    data class Success(val productUiSuccessState: ProductUiSuccessState) : ProductUIState()
    data class Error(val message: String) : ProductUIState()
}
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel(){


    private val _productUIState = MutableStateFlow<ProductUIState>(ProductUIState.Loading)
    val productUIState: StateFlow<ProductUIState> = _productUIState.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts(){
        viewModelScope.launch {
            _productUIState.value = ProductUIState.Loading
            try {
                val result = productRepository.getAllProducts()
                val categories = productRepository.getProductCategories()
                if (result.isSuccess){
                    _productUIState.value = ProductUIState.Success(
                        ProductUiSuccessState(
                            products = MutableStateFlow(result.getOrNull()?: emptyList()),
                            categories = MutableStateFlow(categories.getOrNull()?: emptyList())
                        )
                    )
                } else {
                    _productUIState.value = ProductUIState.Error(
                        message = result.exceptionOrNull()?.message?: "Error Happened $result"
                    )
                }
            } catch (e: Exception) {
                _productUIState.value = ProductUIState.Error(e.message.toString())
            }

        }
    }

    fun getProductsByCategory(category: String){
        viewModelScope.launch {
            try{
                if(category == "All"){
                    getProducts()
                    return@launch
                }
                val result = productRepository.getProductsByCategory(category.lowercase())
                val categories = productRepository.getProductCategories()
                if (result.isSuccess) {
                    _productUIState.value = ProductUIState.Success(
                        ProductUiSuccessState(
                            products = MutableStateFlow(result.getOrNull() ?: emptyList()),
                            categories = MutableStateFlow(categories.getOrNull()?: emptyList())
                        )
                    )
                } else {
                    _productUIState.value = ProductUIState.Error(
                        message = result.exceptionOrNull()?.message ?: "Error Happened $result"
                    )
                }
            } catch (e: Exception){
                _productUIState.value = ProductUIState.Error(
                    message = e.message.toString()
                )
            }
        }
    }



}