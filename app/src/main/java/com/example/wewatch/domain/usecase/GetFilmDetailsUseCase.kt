package com.example.wewatch.domain.usecase

import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.domain.repository.FilmRepository
import javax.inject.Inject

class GetFilmDetailsUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(imdbId: String): OmdbFilmDetails? {
        return repository.getFilmDetails(imdbId)
    }
}