package com.example.wewatch.di

import android.content.Context
import com.example.wewatch.BuildConfig
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.domain.usecase.*
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
        return FilmRepository(database, apiKey)
    }

    @Provides
    @Singleton
    fun provideGetFilmsUseCase(repository: FilmRepository): GetFilmsUseCase {
        return GetFilmsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddFilmUseCase(repository: FilmRepository): AddFilmUseCase {
        return AddFilmUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteSelectedFilmsUseCase(repository: FilmRepository): DeleteSelectedFilmsUseCase {
        return DeleteSelectedFilmsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFilmSelectionUseCase(repository: FilmRepository): ToggleFilmSelectionUseCase {
        return ToggleFilmSelectionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchFilmsUseCase(repository: FilmRepository): SearchFilmsUseCase {
        return SearchFilmsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFilmDetailsUseCase(repository: FilmRepository): GetFilmDetailsUseCase {
        return GetFilmDetailsUseCase(repository)
    }
}