package com.example.wewatch.data.repository

import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class FilmRepository(
    private val db: AppDatabase,
    private val apiKey: String
) {

    // ========== ЛОКАЛЬНЫЕ ОПЕРАЦИИ ==========

    fun getAllFilms(): Flow<List<FilmEntity>> = db.filmDao().getAllFilms()

    suspend fun insertFilm(film: FilmEntity) {
        db.filmDao().insertFilm(film)
    }

    suspend fun deleteSelectedFilms() {
        db.filmDao().deleteSelectedFilms()
    }

    suspend fun updateFilm(film: FilmEntity) {
        db.filmDao().updateFilm(film)
    }

    // ========== УДАЛЕННЫЕ ОПЕРАЦИИ ==========

    suspend fun searchFilms(query: String, year: String? = null): List<OmdbFilm> {
        return try {
            val response = RetrofitInstance.api.searchFilms(
                query = query,
                year = year,
                apiKey = apiKey
            )
            response.Search ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFilmDetails(imdbId: String): OmdbFilmDetails? {
        return try {
            RetrofitInstance.api.getFilmDetails(
                imdbId = imdbId,
                apiKey = apiKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * НОВЫЙ МЕТОД: Поиск фильма с деталями (получаем жанр)
     */
    suspend fun searchFilmWithDetails(query: String, year: String? = null): List<OmdbFilm> {
        val searchResults = searchFilms(query, year)

        // Для каждого фильма пробуем получить жанр
        val resultsWithGenres = mutableListOf<OmdbFilm>()

        for (film in searchResults) {
            try {
                val details = getFilmDetails(film.imdbID)
                val filmWithGenre = film.copy(
                    Genre = details?.Genre ?: "N/A"
                )
                resultsWithGenres.add(filmWithGenre)
            } catch (e: Exception) {
                // Если не удалось получить детали, добавляем без жанра
                resultsWithGenres.add(film)
            }
        }

        return resultsWithGenres
    }
}