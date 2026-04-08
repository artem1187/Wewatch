package com.example.wewatch.domain.usecase

import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.domain.repository.FilmRepository
import javax.inject.Inject

class SearchFilmsUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(query: String, year: String? = null): List<OmdbFilm> {
        return repository.searchFilmWithDetails(query, year)
    }
}