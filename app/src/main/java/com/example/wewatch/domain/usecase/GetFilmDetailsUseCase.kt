package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.data.remote.OmdbFilmDetails

class GetFilmDetailsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(imdbId: String): OmdbFilmDetails? {
        return repository.getFilmDetails(imdbId)
    }
}