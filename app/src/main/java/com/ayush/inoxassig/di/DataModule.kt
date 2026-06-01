package com.ayush.inoxassig.di

import android.content.Context
import com.ayush.inoxassig.data.repository.FoodRepository
import com.ayush.inoxassig.data.repository.FoodRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true // Prevents crashes if JSON has extra fields
            coerceInputValues = true // Handles nulls/missing values gracefully
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        @ApplicationContext context: Context,
        json: Json
    ): FoodRepository {
        return FoodRepositoryImpl(context, json)
    }
}