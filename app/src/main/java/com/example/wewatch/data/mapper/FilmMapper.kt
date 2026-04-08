package com.example.wewatch.data.mapper

import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.model.SearchResult


object FilmMapper {

    // Entity -> Domain
    fun toDomain(entity: FilmEntity): Film {
        return Film(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            posterUrl = entity.posterUrl,
            genre = entity.genre,
            isSelected = entity.isSelected,
            imdbId = entity.imdbId
        )
    }

    // Domain -> Entity
    fun toEntity(domain: Film): FilmEntity {
        return FilmEntity(
            id = domain.id,
            title = domain.title,
            year = domain.year,
            posterUrl = domain.posterUrl,
            genre = domain.genre,
            isSelected = domain.isSelected,
            imdbId = domain.imdbId
        )
    }

    // List<Entity> -> List<Domain>
    fun toDomainList(entities: List<FilmEntity>): List<Film> {
        return entities.map { toDomain(it) }
    }

    // OmdbFilm -> Domain SearchResult
    fun toSearchResult(film: OmdbFilm): SearchResult {
        return SearchResult(
            title = film.Title,
            year = film.Year,
            imdbId = film.imdbID,
            posterUrl = film.Poster,
            genre = film.Genre
        )
    }

    // List<OmdbFilm> -> List<SearchResult>
    fun toSearchResultList(films: List<OmdbFilm>): List<SearchResult> {
        return films.map { toSearchResult(it) }
    }
}