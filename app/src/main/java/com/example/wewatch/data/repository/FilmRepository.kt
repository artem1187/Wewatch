package com.example.wewatch.data.repository

import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.FilmEntity
import kotlinx.coroutines.flow.Flow

class FilmRepository(
    private val db: AppDatabase
) {
    // Получить все фильмы
    fun getAllFilms(): Flow<List<FilmEntity>> = db.filmDao().getAllFilms()

    // Добавить фильм
    suspend fun insertFilm(film: FilmEntity) {
        db.filmDao().insertFilm(film)
    }

    // Удалить выбранные
    suspend fun deleteSelectedFilms() {
        db.filmDao().deleteSelectedFilms()
    }

    // Обновить фильм
    suspend fun updateFilm(film: FilmEntity) {
        db.filmDao().updateFilm(film)
    }
}