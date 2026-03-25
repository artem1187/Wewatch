package com.example.wewatch.presentation.search

import com.example.wewatch.data.remote.OmdbFilm

sealed class SearchIntent {
    data class Search(val query: String, val year: String?) : SearchIntent()
    data class SelectFilm(val film: OmdbFilm) : SearchIntent()
    data object ClearResults : SearchIntent()
    data object NavigateBack : SearchIntent()
}