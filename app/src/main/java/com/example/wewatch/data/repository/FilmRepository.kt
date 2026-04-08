package com.example.wewatch.data.repository

import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.mapper.FilmMapper
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.data.remote.RetrofitInstance
import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.repository.FilmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FilmRepositoryImpl(
    private val database: AppDatabase,
    private val apiKey: String
) : FilmRepository {

    private val filmDao = database.filmDao()

    override fun getAllFilms(): Flow<List<Film>> {
        return filmDao.getAllFilms().map { entities ->
            FilmMapper.toDomainList(entities)
        }
    }

    override suspend fun insertFilm(film: Film) {
        filmDao.insertFilm(FilmMapper.toEntity(film))
    }

    override suspend fun deleteSelectedFilms() {
        filmDao.deleteSelectedFilms()
    }

    override suspend fun updateFilm(film: Film) {
        filmDao.updateFilm(FilmMapper.toEntity(film))
    }

    override suspend fun searchFilms(query: String, year: String?): List<OmdbFilm> {
        return try {
            val response = RetrofitInstance.api.searchFilms(query, year, apiKey)
            response.Search ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getFilmDetails(imdbId: String): OmdbFilmDetails? {
        return try {
            RetrofitInstance.api.getFilmDetails(imdbId, apiKey)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun searchFilmWithDetails(query: String, year: String?): List<OmdbFilm> {
        val searchResults = searchFilms(query, year)
        val resultsWithGenres = mutableListOf<OmdbFilm>()

        for (film in searchResults) {
            try {
                val details = getFilmDetails(film.imdbID)
                val filmWithGenre = film.copy(Genre = details?.Genre ?: "N/A")
                resultsWithGenres.add(filmWithGenre)
            } catch (e: Exception) {
                resultsWithGenres.add(film)
            }
        }

        return resultsWithGenres
    }
}