package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.repository.FilmRepository
import javax.inject.Inject

class AddFilmUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(film: Film) {
        repository.insertFilm(film)
    }
}