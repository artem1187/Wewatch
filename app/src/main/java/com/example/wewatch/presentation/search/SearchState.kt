package com.example.wewatch.presentation.search

import com.example.wewatch.data.remote.OmdbFilm

data class SearchState(
    val searchResults: List<OmdbFilm> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val year: String = ""
)