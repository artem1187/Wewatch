package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.data.remote.OmdbFilm

class SearchFilmsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(query: String, year: String? = null): List<OmdbFilm> {
        return repository.searchFilmWithDetails(query, year)
    }
}