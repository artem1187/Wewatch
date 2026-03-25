package com.example.wewatch.domain.usecase

import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.domain.model.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFilmsUseCase(
    private val repository: FilmRepository
) {
    operator fun invoke(): Flow<List<Film>> {
        return repository.getAllFilms().map { entities ->
            entities.map { Film.fromEntity(it) }
        }
    }
}