package com.example.wewatch.di

import android.content.Context
import com.example.wewatch.BuildConfig
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.repository.FilmRepositoryImpl
import com.example.wewatch.domain.repository.FilmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFilmRepository(
        database: AppDatabase
    ): FilmRepository {
        val apiKey = BuildConfig.OMDB_API_KEY
        return FilmRepositoryImpl(database, apiKey)
    }
}