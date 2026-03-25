package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository

class DeleteSelectedFilmsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedFilms()
    }
}