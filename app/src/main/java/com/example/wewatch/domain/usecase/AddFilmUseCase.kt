package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.domain.model.Film

class AddFilmUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(film: Film) {
        repository.insertFilm(film.toEntity())
    }
}