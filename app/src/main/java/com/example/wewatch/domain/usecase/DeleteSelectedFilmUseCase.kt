package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.repository.FilmRepository
import javax.inject.Inject

class DeleteSelectedFilmsUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedFilms()
    }
}