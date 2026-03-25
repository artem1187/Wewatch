package com.example.wewatch.domain.model

import com.example.wewatch.data.local.FilmEntity

data class Film(
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val genre: String? = null,
    val isSelected: Boolean = false,
    val imdbId: String? = null
) {
    fun toEntity(): FilmEntity = FilmEntity(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl,
        genre = genre,
        isSelected = isSelected,
        imdbId = imdbId
    )

    companion object {
        fun fromEntity(entity: FilmEntity): Film = Film(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            posterUrl = entity.posterUrl,
            genre = entity.genre,
            isSelected = entity.isSelected,
            imdbId = entity.imdbId
        )
    }
}