package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.domain.model.Film

class ToggleFilmSelectionUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(film: Film) {
        repository.updateFilm(film.copy(isSelected = !film.isSelected).toEntity())
    }
}