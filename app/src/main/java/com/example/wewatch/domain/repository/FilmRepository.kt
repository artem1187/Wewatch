package com.example.wewatch.domain.repository

import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.domain.model.Film
import kotlinx.coroutines.flow.Flow


interface FilmRepository {

    // Локальные операции
    fun getAllFilms(): Flow<List<Film>>

    suspend fun insertFilm(film: Film)

    suspend fun deleteSelectedFilms()

    suspend fun updateFilm(film: Film)

    // Удаленные операции (API)
    suspend fun searchFilms(query: String, year: String? = null): List<OmdbFilm>

    suspend fun getFilmDetails(imdbId: String): OmdbFilmDetails?

    suspend fun searchFilmWithDetails(query: String, year: String? = null): List<OmdbFilm>
}