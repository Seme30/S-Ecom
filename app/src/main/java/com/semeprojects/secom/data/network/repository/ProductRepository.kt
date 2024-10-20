package com.semeprojects.secom.data.network.repository

import com.semeprojects.secom.data.network.api.ProductApi
import com.semeprojects.secom.data.network.model.Product

class ProductRepository (private val productDao: ProductApi) {

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = productDao.getAllProducts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: Int): Result<Product> {
        return try {
            val response = productDao.getProductById(productId)
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    Result.success(product)
                } else {
                    Result.failure(Exception("Product not found"))
                }
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val response = productDao.getProductsByCategory(category)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: Int): Result<Unit> {
        return try {
            val response = productDao.deleteProduct(productId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: Int, product: Product): Result<Product>{
        return try {
            val response = productDao.updateProduct(product = product, productId = productId)
            if(response.isSuccessful){
                val updatedProduct = response.body()
                if (updatedProduct != null) {
                    Result.success(updatedProduct)
                } else {
                    Result.failure(Exception("Product not found"))
                }
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception){
            Result.failure(e)
            }
        }

    suspend fun getProductCategories(): Result<List<String>>{
        return try {
            val response = productDao.getCategories()
            if (response.isSuccessful){
                val categories = response.body() ?: emptyList()
                Result.success(categories)
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
            } catch (e: Exception){
                Result.failure(e)
        }
    }

}