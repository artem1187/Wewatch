package com.example.wewatch.presentation.search

import com.example.wewatch.data.remote.OmdbFilm

sealed class SearchEffect {
    data class FilmSelected(val film: OmdbFilm) : SearchEffect()
    data object NavigateBack : SearchEffect()
    data class ShowError(val message: String) : SearchEffect()
}