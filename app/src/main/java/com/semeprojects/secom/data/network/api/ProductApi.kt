package com.semeprojects.secom.data.network.api

import com.semeprojects.secom.data.network.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.DELETE


interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<Product>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") productId: Int, @Body product: Product): Response<Product>

    @DELETE(
        "products/{id}")
    suspend fun deleteProduct(@Path("id") productId: Int): Response<Void>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

}