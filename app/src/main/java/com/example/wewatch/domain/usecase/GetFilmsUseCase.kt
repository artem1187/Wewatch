package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.repository.FilmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case для получения всех фильмов
 * Инкапсулирует бизнес-логику получения фильмов
 */
class GetFilmsUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    operator fun invoke(): Flow<List<Film>> {
        return repository.getAllFilms()
    }
}