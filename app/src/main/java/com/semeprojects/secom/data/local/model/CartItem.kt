package com.semeprojects.secom.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val productId: Int,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val imageUrl: String
)
