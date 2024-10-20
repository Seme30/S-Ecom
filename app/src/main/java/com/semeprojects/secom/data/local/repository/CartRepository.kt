package com.semeprojects.secom.data.local.repository

import com.semeprojects.secom.data.local.dao.CartDao
import com.semeprojects.secom.data.local.model.CartItem
import com.semeprojects.secom.data.network.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao) {

    fun getAllCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()


    suspend fun addToCart(product: Product) {
        val existingItem = cartDao.getCartItemById(product.id)
        if (existingItem != null) {
            cartDao.incrementQuantity(product.id)
        } else {
            cartDao.insertCartItem(CartItem(product.id, product.title, product.price, 1,  product.image))
        }
    }

    suspend fun removeFromCart(productId: Int) {
        cartDao.deleteCartItem(productId)
    }

    suspend fun incrementQuantity(productId: Int) {
        cartDao.incrementQuantity(productId)
    }

    suspend fun decrementQuantity(productId: Int) {
        val item = cartDao.getCartItemById(productId)
        if (item != null && item.quantity > 1) {
            cartDao.decrementQuantity(productId)
        } else {
            cartDao.deleteCartItem(productId)
        }
    }



    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
