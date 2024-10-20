package com.semeprojects.secom.domain.di

import android.app.Application
import com.semeprojects.secom.data.preferences.ThemeDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Provides
    @Singleton
    fun provideThemeDataStoreRepository(
        application: Application
    ) = ThemeDataStoreRepository(context = application)

}