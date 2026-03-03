package com.example.wewatch.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс для работы с OMDb API
 * Retrofit автоматически реализует эти методы
 */
interface OmdbApiService {

    /**
     * Поиск фильмов по ключевому слову
     * @param query - поисковый запрос (обязательный)
     * @param year - год выпуска (опциональный)
     * @param apiKey - ваш ключ API
     */
    @GET("/")  // GET запрос на корневой URL
    suspend fun searchFilms(
        @Query("s") query: String,           // параметр s - поиск
        @Query("y") year: String? = null,    // параметр y - год (может быть null)
        @Query("apikey") apiKey: String       // параметр apikey
    ): OmdbSearchResponse

    /**
     * Получение детальной информации о фильме по IMDB ID
     * @param imdbId - ID фильма (например, tt1285016)
     */
    @GET("/")
    suspend fun getFilmDetails(
        @Query("i") imdbId: String,           // параметр i - IMDB ID
        @Query("apikey") apiKey: String
    ): OmdbFilmDetails
}