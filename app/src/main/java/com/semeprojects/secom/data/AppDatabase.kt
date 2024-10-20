package com.semeprojects.secom.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.semeprojects.secom.data.local.dao.CartDao
import com.semeprojects.secom.data.local.model.CartItem

@Database(entities = [CartItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
