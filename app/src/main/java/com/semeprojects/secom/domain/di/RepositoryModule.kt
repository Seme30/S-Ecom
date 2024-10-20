package com.semeprojects.secom.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.semeprojects.secom.data.local.dao.CartDao
import com.semeprojects.secom.data.local.repository.CartRepository
import com.semeprojects.secom.data.network.api.ProductApi
import com.semeprojects.secom.data.network.repository.FirebaseUserRepository
import com.semeprojects.secom.data.network.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideFirebaseUserRepository(
        firebaseAuth: FirebaseAuth
    ): FirebaseUserRepository = FirebaseUserRepository(firebaseAuth)

    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository {
        return ProductRepository(productApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }

}