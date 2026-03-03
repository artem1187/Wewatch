package com.example.wewatch.data.repository

import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий объединяет локальные и удаленные источники данных
 * @param db - локальная база данных Room
 * @param apiKey - ключ для доступа к OMDb API
 */
class FilmRepository(
    private val db: AppDatabase,
    private val apiKey: String
) {

    // ========== ЛОКАЛЬНЫЕ ОПЕРАЦИИ (Room) ==========

    /**
     * Получить все фильмы из локальной БД
     */
    fun getAllFilms(): Flow<List<FilmEntity>> = db.filmDao().getAllFilms()

    /**
     * Добавить фильм в локальную БД
     */
    suspend fun insertFilm(film: FilmEntity) {
        db.filmDao().insertFilm(film)
    }

    /**
     * Удалить выбранные фильмы
     */
    suspend fun deleteSelectedFilms() {
        db.filmDao().deleteSelectedFilms()
    }

    /**
     * Обновить фильм (например, изменить isSelected)
     */
    suspend fun updateFilm(film: FilmEntity) {
        db.filmDao().updateFilm(film)
    }

    // ========== УДАЛЕННЫЕ ОПЕРАЦИИ (OMDb API) ==========

    /**
     * Поиск фильмов через OMDb API
     * @param query - поисковый запрос
     * @param year - год выпуска (опционально)
     * @return список найденных фильмов или пустой список при ошибке
     */
    suspend fun searchFilms(query: String, year: String? = null): List<OmdbFilm> {
        return try {
            val response = RetrofitInstance.api.searchFilms(
                query = query,
                year = year,
                apiKey = apiKey
            )
            // Если поиск успешен, возвращаем список, иначе пустой список
            response.Search ?: emptyList()
        } catch (e: Exception) {
            // Логируем ошибку в консоль
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Получить детальную информацию о фильме по IMDB ID
     * @param imdbId - ID фильма
     * @return детальная информация или null при ошибке
     */
    suspend fun getFilmDetails(imdbId: String): OmdbFilm? {
        return try {
            val details = RetrofitInstance.api.getFilmDetails(
                imdbId = imdbId,
                apiKey = apiKey
            )
            // Преобразуем детальную информацию в OmdbFilm
            OmdbFilm(
                Title = details.Title,
                Year = details.Year,
                imdbID = imdbId,
                Type = "movie",
                Poster = details.Poster,
                Genre = details.Genre
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}